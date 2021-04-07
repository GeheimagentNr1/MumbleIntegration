package de.geheimagentnr1.mumbleintegration.linking;

import com.skaggsm.jmumblelink.MumbleLink;
import com.skaggsm.jmumblelink.MumbleLinkImpl;
import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.dimension.DimensionType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.regex.Pattern;


public class MumbleLinker {
	
	
	@Nonnull
	private static final Logger LOGGER = LogManager.getLogger( MumbleLinker.class );
	
	@Nonnull
	private static final Pattern UNDERSCORE_PATTERN = Pattern.compile( "_" );
	
	@Nullable
	private static MumbleLink mumble = null;
	
	@Nullable
	private static DimensionType dimension = null;
	
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
		ClientWorld world = minecraft.world;
		ClientPlayerEntity player = minecraft.player;
		
		if( world != null && player != null ) {
			ensureLinking();
			Objects.requireNonNull( mumble );
			DimensionType worldDimension = world.getDimension().getType();
			autoConnect( worldDimension );
			ActiveRenderInfo activeRenderInfo = minecraft.gameRenderer.getActiveRenderInfo();
			float[] camPos = vec3dToArray( activeRenderInfo.getProjectedView() );
			float[] camDir = vec3fToArray( activeRenderInfo.getViewVector() );
			float[] camTop = new float[] { 0.0F, 1.0F, 0.0F };
			if( !ClientConfig.useDimensionChannels() ) {
				camPos[1] += worldDimension.getId() << 9;
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
	
	private static synchronized void autoConnect( @Nonnull DimensionType worldDimension ) {
		
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
	
	private static void connectToMumble( @Nonnull DimensionType dimensionType ) {
		
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
						buildMumblePath( dimensionType ),
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
	private static String buildMumblePath( @Nonnull DimensionType dimensionType ) {
		
		String path = "/" + ClientConfig.getPath();
		
		if( !ClientConfig.useDimensionChannels() ) {
			return path;
		}
		return path + "/" + getTrimedNameOfDimension( dimensionType );
	}
	
	@Nonnull
	private static String getTrimedNameOfDimension( @Nonnull DimensionType dimensionType ) {
		
		return StringUtils.capitalize( UNDERSCORE_PATTERN.matcher(
			Objects.requireNonNull( dimensionType.getRegistryName() ).getPath() ).replaceAll( " " )
		);
	}
	
	private static float[] vec3dToArray( @Nonnull Vec3d vec3d ) {
		
		return vec3ToArray( (float)vec3d.x, (float)vec3d.y, -(float)vec3d.z );
	}
	
	private static float[] vec3fToArray( @Nonnull Vector3f vector3f ) {
		
		return vec3ToArray( vector3f.getX(), vector3f.getY(), -vector3f.getZ() );
	}
	
	private static float[] vec3ToArray( float x, float y, float z ) {
		
		return new float[] { x, y, z };
	}
}
