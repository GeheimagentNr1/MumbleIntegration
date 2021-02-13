package de.geheimagentnr1.mumbleintegration.linking;

import com.skaggsm.jmumblelink.MumbleLink;
import com.skaggsm.jmumblelink.MumbleLinkImpl;
import de.geheimagentnr1.mumbleintegration.config.MainConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.renderer.ActiveRenderInfo;
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


@SuppressWarnings( { "SynchronizationOnStaticField", "NonThreadSafeLazyInitialization" } )
public class MumbleLinking {
	
	
	@Nonnull
	private static final Logger LOGGER = LogManager.getLogger();
	
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
				DimensionType worldDimension = world.getDimension().getType();
				autoConnect( worldDimension );
				ActiveRenderInfo activeRenderInfo = minecraft.gameRenderer.getActiveRenderInfo();
				float[] camPos = vec3dToArray( activeRenderInfo.getProjectedView() );
				float[] camDir = vec3dToArray( activeRenderInfo.getLookDirection() );
				float[] camTop = new float[] { 0.0F, 1.0F, 0.0F };
				if( !MainConfig.useDimensionChannels() ) {
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
	}
	
	private static void autoConnect( @Nonnull DimensionType worldDimension ) {
		
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
	
	private static void connectToMumble( @Nonnull DimensionType dimensionType ) {
		
		try {
			if( Desktop.isDesktopSupported() ) {
				Desktop desktop = Desktop.getDesktop();
				if( desktop.isSupported( Desktop.Action.BROWSE ) ) {
					desktop.browse( new URI( "mumble", null, MainConfig.getAddress(), MainConfig.getPort(),
						buildMumblePath( dimensionType ), null, null ) );
				}
			}
		} catch( IOException | URISyntaxException | HeadlessException exception ) {
			LOGGER.error( "Connection To Mumble Failed", exception );
		}
	}
	
	@Nonnull
	private static String buildMumblePath( @Nonnull DimensionType dimensionType ) {
		
		String path = "/" + MainConfig.getPath();
		
		if( !MainConfig.useDimensionChannels() ) {
			return path;
		}
		return path + "/" + getTrimedNameOfDimension( dimensionType );
	}
	
	@Nonnull
	private static String getTrimedNameOfDimension( @Nonnull DimensionType dimensionType ) {
		
		return StringUtils.capitalize(
			UNDERSCORE_PATTERN.matcher( Objects.requireNonNull( dimensionType.getRegistryName() ).getPath() )
				.replaceAll( " " ) );
	}
	
	private static float[] vec3dToArray( @Nonnull Vec3d vec3d ) {
		
		return new float[] { (float)vec3d.x, (float)vec3d.y, -(float)vec3d.z };
	}
}
