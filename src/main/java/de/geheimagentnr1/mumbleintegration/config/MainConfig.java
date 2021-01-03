package de.geheimagentnr1.mumbleintegration.config;

import de.geheimagentnr1.mumbleintegration.linking.MumbleLinking;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;


public class MainConfig {
	
	
	@Nonnull
	private static final Logger LOGGER = LogManager.getLogger();
	
	@Nonnull
	public static final String mod_name = "Mumble Integration";
	
	@Nonnull
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	@Nonnull
	public static final ForgeConfigSpec CONFIG;
	
	@Nonnull
	public static final ForgeConfigSpec.BooleanValue MUMBLE_ACTIVE;
	
	@Nonnull
	public static final ForgeConfigSpec.BooleanValue AUTO_CONNECT;
	
	@Nonnull
	public static final ForgeConfigSpec.ConfigValue<String> ADDRESS;
	
	@Nonnull
	public static final ForgeConfigSpec.IntValue PORT;
	
	@Nonnull
	public static final ForgeConfigSpec.ConfigValue<String> PATH;
	
	@Nonnull
	public static final ForgeConfigSpec.BooleanValue USE_DIMENSION_CHANNELS;
	
	static {
		
		MUMBLE_ACTIVE = BUILDER.comment( "Should the mumble integration be active?" ).define( "mumble_active", false );
		AUTO_CONNECT = BUILDER.comment( "Should mumble be connect automated?" ).define( "auto_connect", false );
		ADDRESS = BUILDER.comment( "Address of the mumble server." ).define( "address", "" );
		PORT = BUILDER.comment( "Port of the mumble server." ).defineInRange( "port", 64738, 0, 65535 );
		PATH = BUILDER.comment( "Path of the mumble channel." ).define( "path", "" );
		USE_DIMENSION_CHANNELS = BUILDER.comment( "Use subchannels for each dimension?" )
			.define( "use_dimension_channels", false );
		
		CONFIG = BUILDER.build();
	}
	
	public static void handleConfigChange() {
		
		printConfig();
		if( MUMBLE_ACTIVE.get() ) {
			MumbleLinking.link();
		} else {
			MumbleLinking.unlink();
		}
	}
	
	private static void printConfig() {
		
		LOGGER.info( "Loading \"{}\" Config", mod_name );
		LOGGER.info( "{} = {}", MUMBLE_ACTIVE.getPath(), MUMBLE_ACTIVE.get() );
		LOGGER.info( "{} = {}", AUTO_CONNECT.getPath(), AUTO_CONNECT.get() );
		LOGGER.info( "{} = {}", ADDRESS.getPath(), ADDRESS.get() );
		LOGGER.info( "{} = {}", PORT.getPath(), PORT.get() );
		LOGGER.info( "{} = {}", PATH.getPath(), PATH.get() );
		LOGGER.info( "{} = {}", USE_DIMENSION_CHANNELS.getPath(), USE_DIMENSION_CHANNELS.get() );
		LOGGER.info( "\"{}\" Config loaded", mod_name );
	}
	
	public static boolean isMumbleActive() {
		
		return MUMBLE_ACTIVE.get();
	}
	
	public static boolean shouldAutoConnect() {
		
		return AUTO_CONNECT.get();
	}
	
	@Nonnull
	public static String getAddress() {
		
		return ADDRESS.get();
	}
	
	public static int getPort() {
		
		return PORT.get();
	}
	
	@Nonnull
	public static String getPath() {
		
		return PATH.get();
	}
	
	public static boolean useDimensionChannels() {
		
		return USE_DIMENSION_CHANNELS.get();
	}
}
