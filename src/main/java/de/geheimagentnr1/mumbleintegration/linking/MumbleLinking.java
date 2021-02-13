package de.geheimagentnr1.mumbleintegration.linking;

import com.skaggsm.jmumblelink.MumbleLink;
import com.skaggsm.jmumblelink.MumbleLinkImpl;
import de.geheimagentnr1.mumbleintegration.config.MainConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
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


@SuppressWarnings( { "SynchronizationOnStaticField", "NonThreadSafeLazyInitialization" } )
public class MumbleLinking {
	
	
	@Nonnull
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Nonnull
	private static final Pattern UNDERSCORE_PATTERN = Pattern.compile( "_" );
	
	@Nullable
	private static MumbleLink mumble = null;
	
	@Nullable
	private static RegistryKey<World> dimension = null;
	
	static {
		// Required to open URIs
		//noinspection AccessOfSystemProperties
		System.setProperty( "java.awt.headless", "false" );
	}
	
	public static void link() {
		
		synchronized( UNDERSCORE_PATTERN ) {
			ensureLinking();
		}
	}
	
	private static void ensureLinking() {
		
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
		
		synchronized( UNDERSCORE_PATTERN ) {
			ensureUnlinking();
		}
	}
	
	private static void ensureUnlinking() {
		
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
	
	public static void updateData() {
		
		if( !MainConfig.isMumbleActive() ) {
			return;
		}
		Minecraft minecraft = Minecraft.getInstance();
		ClientWorld world = minecraft.world;
		ClientPlayerEntity player = minecraft.player;
		
		if( world != null && player != null ) {
			synchronized( UNDERSCORE_PATTERN ) {
				ensureLinking();
				Objects.requireNonNull( mumble );
				RegistryKey<World> worldDimension = world.func_234923_W_();
				autoConnect( worldDimension );
				ActiveRenderInfo activeRenderInfo = minecraft.gameRenderer.getActiveRenderInfo();
				float[] camPos = vec3dToArray( activeRenderInfo.getProjectedView() );
				float[] camDir = vec3fToArray( activeRenderInfo.getViewVector() );
				float[] camTop = new float[] { 0.0F, 1.0F, 0.0F };
				if( !MainConfig.useDimensionChannels() ) {
					List<RegistryKey<World>> worlds = Objects.requireNonNull( Minecraft.getInstance().getConnection() )
						.func_239164_m_().stream().sorted().collect( Collectors.toList() );
					
					int index = -1;
					for( int i = 0; i < worlds.size(); i++ ) {
						if( worlds.get( i ).equals( world.func_234923_W_() ) ) {
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
				mumble.setIdentity( player.getUniqueID().toString() );
			}
		}
	}
	
	private static void autoConnect( @Nonnull RegistryKey<World> worldDimension ) {
		
		if( MainConfig.shouldAutoConnect() ) {
			if( MainConfig.useDimensionChannels() ) {
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
	
	private static void connectToMumble( @Nonnull RegistryKey<World> dimensionKey ) {
		
		try {
			if( Desktop.isDesktopSupported() ) {
				Desktop desktop = Desktop.getDesktop();
				if( desktop.isSupported( Desktop.Action.BROWSE ) ) {
					desktop.browse( new URI( "mumble", null, MainConfig.getAddress(), MainConfig.getPort(),
						buildMumblePath( dimensionKey ), null, null ) );
				}
			}
		} catch( IOException | URISyntaxException | HeadlessException exception ) {
			LOGGER.error( "Connection To Mumble Failed", exception );
		}
	}
	
	@Nonnull
	private static String buildMumblePath( @Nonnull RegistryKey<World> dimensionKey ) {
		
		String path = "/" + MainConfig.getPath();
		
		if( !MainConfig.useDimensionChannels() ) {
			return path;
		}
		return path + "/" + getTrimedNameOfDimension( dimensionKey );
	}
	
	@Nonnull
	private static String getTrimedNameOfDimension( @Nonnull RegistryKey<World> dimensionKey ) {
		
		return StringUtils.capitalize( UNDERSCORE_PATTERN.matcher( Objects.requireNonNull(
			dimensionKey.func_240901_a_() ).getPath() ).replaceAll( " " ) );
	}
	
	private static float[] vec3dToArray( @Nonnull Vector3d vec3d ) {
		
		return vec3ToArray( (float)vec3d.x, (float)vec3d.y, -(float)vec3d.z );
	}
	
	private static float[] vec3fToArray( @Nonnull Vector3f vector3f ) {
		
		return vec3ToArray( vector3f.getX(), vector3f.getY(), -vector3f.getZ() );
	}
	
	private static float[] vec3ToArray( float x, float y, float z ) {
		
		return new float[] { x, y, z };
	}
}
