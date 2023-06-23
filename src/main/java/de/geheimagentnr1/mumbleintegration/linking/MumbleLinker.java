package de.geheimagentnr1.mumbleintegration.linking;

import com.skaggsm.jmumblelink.MumbleLink;
import com.skaggsm.jmumblelink.MumbleLinkImpl;
import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import de.geheimagentnr1.minecraft_forge_api.events.ForgeEventHandlerInterface;
import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


@Log4j2
@RequiredArgsConstructor
public class MumbleLinker implements ForgeEventHandlerInterface {
	
	
	@NotNull
	private static final Pattern UNDERSCORE_PATTERN = Pattern.compile( "_" );
	
	@NotNull
	private final AbstractMod abstractMod;
	
	private ClientConfig clientConfig;
	
	@Nullable
	private MumbleLink mumble = null;
	
	@Nullable
	private ResourceKey<Level> dimension = null;
	
	static {
		// Required to open URIs
		//noinspection AccessOfSystemProperties
		System.setProperty( "java.awt.headless", "false" );
	}
	
	@NotNull
	private ClientConfig clientConfig() {
		
		if( clientConfig == null ) {
			clientConfig = abstractMod.getConfig( ModConfig.Type.CLIENT, ClientConfig.class )
				.orElseThrow( () -> new IllegalStateException( "MumbleIntgration#ClientConfig not found" ) );
		}
		return clientConfig;
	}
	
	public void link() {
		
		if( clientConfig().isMumbleActive() ) {
			ensureLinking();
		}
	}
	
	private synchronized void ensureLinking() {
		
		if( mumble == null ) {
			log.info( "Linking to VoIP client..." );
			mumble = new MumbleLinkImpl();
			mumble.setUiVersion( 2 );
			mumble.setName( "Minecraft Mumble Integration Mod" );
			mumble.setContext( "Minecraft" );
			mumble.setDescription( "A Minecraft mod that provides position data to VoIP clients." );
			log.info( "Linked" );
		}
	}
	
	public void unlink() {
		
		ensureUnlinking();
	}
	
	private synchronized void ensureUnlinking() {
		
		if( mumble != null ) {
			log.info( "Unlinking from VoIP client..." );
			try {
				mumble.close();
			} catch( IOException exception ) {
				log.error( "Failed to close mumble connection", exception );
			}
			mumble = null;
			log.info( "Unlinked" );
		}
		dimension = null;
	}
	
	private synchronized void updateData() {
		
		if( !clientConfig().isMumbleActive() ) {
			return;
		}
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		LocalPlayer player = minecraft.player;
		
		if( level != null && player != null ) {
			ensureLinking();
			ResourceKey<Level> worldDimension = level.dimension();
			autoConnect( worldDimension );
			Camera camera = minecraft.gameRenderer.getMainCamera();
			float[] camPos = vec3dToArray( camera.getPosition() );
			float[] camDir = vec3fToArray( camera.getLookVector() );
			float[] camTop = vec3fToArray( camera.getUpVector() );
			if( !clientConfig().useDimensionChannels() ) {
				List<ResourceKey<Level>> worlds = Objects.requireNonNull( Minecraft.getInstance().getConnection() )
					.levels()
					.stream()
					.sorted()
					.toList();
				
				int index = -1;
				for( int i = 0; i < worlds.size(); i++ ) {
					if( worlds.get( i ).equals( level.dimension() ) ) {
						index = i;
					}
				}
				camPos[1] += index << 9;
			}
			Objects.requireNonNull( mumble );
			mumble.incrementUiTick();
			mumble.setAvatarPosition( camPos );
			mumble.setAvatarFront( camDir );
			mumble.setAvatarTop( camTop );
			mumble.setCameraPosition( camPos );
			mumble.setCameraFront( camDir );
			mumble.setCameraTop( camTop );
			mumble.setIdentity( player.getStringUUID() );
		}
	}
	
	private synchronized void autoConnect( @NotNull ResourceKey<Level> worldDimension ) {
		
		if( clientConfig().shouldAutoConnect() ) {
			if( clientConfig().useDimensionChannels() ) {
				if( dimension != worldDimension ) {
					dimension = worldDimension;
					connectToMumble( dimension );
				}
			} else {
				if( dimension == null ) {
					dimension = worldDimension;
					connectToMumble( dimension );
				}
			}
		}
	}
	
	private void connectToMumble( @NotNull ResourceKey<Level> dimensionKey ) {
		
		try {
			if( Desktop.isDesktopSupported() ) {
				Desktop desktop = Desktop.getDesktop();
				if( desktop.isSupported( Desktop.Action.BROWSE ) ) {
					log.info( "Auto Connecting to mumble" );
					desktop.browse( new URI(
						"mumble",
						null,
						clientConfig().getAddress(),
						clientConfig().getPort(),
						buildMumblePath( dimensionKey ),
						null,
						null
					) );
				} else {
					log.warn( "Auto Connect failed: Desktop Api browse action not supported" );
				}
			} else {
				log.warn( "Auto Connect failed: Desktop Api not supported" );
			}
		} catch( IOException | URISyntaxException | HeadlessException exception ) {
			log.error( "Connection To Mumble Failed", exception );
		}
	}
	
	@NotNull
	private String buildMumblePath( @NotNull ResourceKey<Level> dimensionKey ) {
		
		String path = "/" + clientConfig().getPath();
		
		if( !clientConfig().useDimensionChannels() ) {
			return path;
		}
		return path + "/" + getTrimedNameOfDimension( dimensionKey );
	}
	
	@NotNull
	private String getTrimedNameOfDimension( @NotNull ResourceKey<Level> dimensionKey ) {
		
		return StringUtils.capitalize( UNDERSCORE_PATTERN.matcher(
			Objects.requireNonNull( dimensionKey.location() ).getPath() ).replaceAll( " " )
		);
	}
	
	private float[] vec3dToArray( @NotNull Vec3 vec3d ) {
		
		return vec3ToArray( (float)vec3d.x, (float)vec3d.y, -(float)vec3d.z );
	}
	
	private float[] vec3fToArray( @NotNull Vector3f vector3f ) {
		
		return vec3ToArray( vector3f.x(), vector3f.y(), -vector3f.z() );
	}
	
	private float[] vec3ToArray( float x, float y, float z ) {
		
		return new float[] { x, y, z };
	}
	
	@Override
	public void handlePlayerLoggedInEvent( @NotNull PlayerEvent.PlayerLoggedInEvent event ) {
		
		link();
	}
	
	@SubscribeEvent
	@Override
	public void handleClientPlayerNetworkLoggingOutEvent( @NotNull ClientPlayerNetworkEvent.LoggingOut event ) {
		
		unlink();
	}
	
	@SubscribeEvent
	@Override
	public void handleClientTickEvent( @NotNull TickEvent.ClientTickEvent event ) {
		
		try {
			updateData();
		} catch( @SuppressWarnings( "ProhibitedExceptionCaught" ) NullPointerException exception ) {
			log.error( "Error during mumble data update", exception );
		}
	}
}
