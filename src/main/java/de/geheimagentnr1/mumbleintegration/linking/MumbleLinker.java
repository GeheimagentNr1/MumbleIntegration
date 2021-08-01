package de.geheimagentnr1.mumbleintegration.linking;

import com.mojang.math.Vector3f;
import com.skaggsm.jmumblelink.MumbleLink;
import com.skaggsm.jmumblelink.MumbleLinkImpl;
import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


public class MumbleLinker {
	
	
	@Nonnull
	private static final Logger LOGGER = LogManager.getLogger( MumbleLinker.class );
	
	@Nonnull
	private static final Pattern UNDERSCORE_PATTERN = Pattern.compile( "_" );
	
	@Nullable
	private static MumbleLink mumble = null;
	
	@Nullable
	private static ResourceKey<Level> dimension = null;
	
	static {
		// Required to open URIs
		//noinspection AccessOfSystemProperties
		System.setProperty( "java.awt.headless", "false" );
	}
	
	public static void link() {
		
		ensureLinking();
	}
	
	private static synchronized void ensureLinking() {
		
		if( mumble == null ) {
			LOGGER.info( "Linking to VoIP client..." );
			mumble = new MumbleLinkImpl();
			mumble.setUiVersion( 2 );
			mumble.setName( "Minecraft Mumble Integration Mod" );
			mumble.setContext( "Minecraft" );
			mumble.setDescription( "A Minecraft mod that provides position data to VoIP clients." );
			LOGGER.info( "Linked" );
		}
	}
	
	public static void unlink() {
		
		ensureUnlinking();
	}
	
	private static synchronized void ensureUnlinking() {
		
		if( mumble != null ) {
			LOGGER.info( "Unlinking from VoIP client..." );
			try {
				mumble.close();
			} catch( IOException exception ) {
				LOGGER.error( "Failed to close mumble connection", exception );
			}
			mumble = null;
			LOGGER.info( "Unlinked" );
		}
		dimension = null;
	}
	
	public static synchronized void updateData() {
		
		if( !ClientConfig.isMumbleActive() ) {
			return;
		}
		Minecraft minecraft = Minecraft.getInstance();
		ClientLevel level = minecraft.level;
		LocalPlayer player = minecraft.player;
		
		if( level != null && player != null ) {
			ensureLinking();
			Objects.requireNonNull( mumble );
			ResourceKey<Level> worldDimension = level.dimension();
			autoConnect( worldDimension );
			Camera camera = minecraft.gameRenderer.getMainCamera();
			float[] camPos = vec3dToArray( camera.getPosition() );
			float[] camDir = vec3fToArray( camera.getLookVector() );
			float[] camTop = vec3fToArray( camera.getUpVector() );
			if( !ClientConfig.useDimensionChannels() ) {
				List<ResourceKey<Level>> worlds = Objects.requireNonNull( Minecraft.getInstance().getConnection() )
					.levels()
					.stream()
					.sorted()
					.collect( Collectors.toList() );
				
				int index = -1;
				for( int i = 0; i < worlds.size(); i++ ) {
					if( worlds.get( i ).equals( level.dimension() ) ) {
						index = i;
					}
				}
				camPos[1] += index << 9;
			}
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
	
	private static synchronized void autoConnect( @Nonnull ResourceKey<Level> worldDimension ) {
		
		if( ClientConfig.shouldAutoConnect() ) {
			if( ClientConfig.useDimensionChannels() ) {
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
	
	private static void connectToMumble( @Nonnull ResourceKey<Level> dimensionKey ) {
		
		try {
			if( Desktop.isDesktopSupported() ) {
				Desktop desktop = Desktop.getDesktop();
				if( desktop.isSupported( Desktop.Action.BROWSE ) ) {
					LOGGER.info( "Auto Connecting to mumble" );
					desktop.browse( new URI(
						"mumble",
						null,
						ClientConfig.getAddress(),
						ClientConfig.getPort(),
						buildMumblePath( dimensionKey ),
						null,
						null
					) );
				} else {
					LOGGER.warn( "Auto Connect failed: Desktop Api browse action not supported" );
				}
			} else {
				LOGGER.warn( "Auto Connect failed: Desktop Api not supported" );
			}
		} catch( IOException | URISyntaxException | HeadlessException exception ) {
			LOGGER.error( "Connection To Mumble Failed", exception );
		}
	}
	
	@Nonnull
	private static String buildMumblePath( @Nonnull ResourceKey<Level> dimensionKey ) {
		
		String path = "/" + ClientConfig.getPath();
		
		if( !ClientConfig.useDimensionChannels() ) {
			return path;
		}
		return path + "/" + getTrimedNameOfDimension( dimensionKey );
	}
	
	@Nonnull
	private static String getTrimedNameOfDimension( @Nonnull ResourceKey<Level> dimensionKey ) {
		
		return StringUtils.capitalize( UNDERSCORE_PATTERN.matcher(
			Objects.requireNonNull( dimensionKey.location() ).getPath() ).replaceAll( " " )
		);
	}
	
	private static float[] vec3dToArray( @Nonnull Vec3 vec3d ) {
		
		return vec3ToArray( (float)vec3d.x, (float)vec3d.y, -(float)vec3d.z );
	}
	
	private static float[] vec3fToArray( @Nonnull Vector3f vector3f ) {
		
		return vec3ToArray( vector3f.x(), vector3f.y(), -vector3f.z() );
	}
	
	private static float[] vec3ToArray( float x, float y, float z ) {
		
		return new float[] { x, y, z };
	}
}
