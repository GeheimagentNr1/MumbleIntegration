package de.geheimagentnr1.mumbleintegration.config;

import de.geheimagentnr1.mumbleintegration.linking.MumbleLinker;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;


public class ClientConfig {
	
	
	@Nonnull
	private static final Logger LOGGER = LogManager.getLogger( ClientConfig.class );
	
	@Nonnull
	private static final String MOD_NAME = ModLoadingContext.get().getActiveContainer().getModInfo().getDisplayName();
	
	@Nonnull
	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	
	@Nonnull
	public static final ForgeConfigSpec CONFIG;
	
	@Nonnull
	private static final ForgeConfigSpec.BooleanValue MUMBLE_ACTIVE;
	
	@Nonnull
	private static final ForgeConfigSpec.BooleanValue AUTO_CONNECT;
	
	@Nonnull
	private static final ForgeConfigSpec.ConfigValue<String> ADDRESS;
	
	@Nonnull
	private static final ForgeConfigSpec.IntValue PORT;
	
	@Nonnull
	private static final ForgeConfigSpec.ConfigValue<String> PATH;
	
	@Nonnull
	private static final ForgeConfigSpec.BooleanValue USE_DIMENSION_CHANNELS;
	
	static {
		
		MUMBLE_ACTIVE = BUILDER.comment( "Should the Mumble integration be active?" )
			.define( "mumble_active", false );
		AUTO_CONNECT = BUILDER.comment( "Should Mumble be connect automated?" )
			.define( "auto_connect", false );
		ADDRESS = BUILDER.comment( "Address of the Mumble server." )
			.define( "address", "" );
		PORT = BUILDER.comment( "Port of the Mumble server." )
			.defineInRange( "port", 64738, 0, 65535 );
		PATH = BUILDER.comment( "Path of the Mumble channel." )
			.define( "path", "" );
		USE_DIMENSION_CHANNELS = BUILDER.comment( "Use subchannels for each dimension?" )
			.define( "use_dimension_channels", false );
		
		CONFIG = BUILDER.build();
	}
	
	public static void handleConfigChange() {
		
		printConfig();
		if( MUMBLE_ACTIVE.get() ) {
			MumbleLinker.link();
		} else {
			MumbleLinker.unlink();
		}
	}
	
	private static void printConfig() {
		
		LOGGER.info( "Loading \"{}\" Client Config", MOD_NAME );
		LOGGER.info( "{} = {}", MUMBLE_ACTIVE.getPath(), MUMBLE_ACTIVE.get() );
		LOGGER.info( "{} = {}", AUTO_CONNECT.getPath(), AUTO_CONNECT.get() );
		LOGGER.info( "{} = {}", ADDRESS.getPath(), ADDRESS.get() );
		LOGGER.info( "{} = {}", PORT.getPath(), PORT.get() );
		LOGGER.info( "{} = {}", PATH.getPath(), PATH.get() );
		LOGGER.info( "{} = {}", USE_DIMENSION_CHANNELS.getPath(), USE_DIMENSION_CHANNELS.get() );
		LOGGER.info( "\"{}\" Client Config loaded", MOD_NAME );
	}
	
	@Nonnull
	public static String getModName() {
		
		return MOD_NAME;
	}
	
	public static boolean isMumbleActive() {
		
		return MUMBLE_ACTIVE.get();
	}
	
	public static void setMumbleActive( boolean mumbleActive ) {
		
		MUMBLE_ACTIVE.set( mumbleActive );
	}
	
	public static boolean shouldAutoConnect() {
		
		return AUTO_CONNECT.get();
	}
	
	public static void setAutoConnect( boolean autoConnect ) {
		
		AUTO_CONNECT.set( autoConnect );
	}
	
	@Nonnull
	public static String getAddress() {
		
		return ADDRESS.get();
	}
	
	public static void setAddress( String address ) {
		
		ADDRESS.set( address );
	}
	
	public static int getPort() {
		
		return PORT.get();
	}
	
	public static void setPort( int port ) {
		
		PORT.set( port );
	}
	
	@Nonnull
	public static String getPath() {
		
		return PATH.get();
	}
	
	public static void setPath( String path ) {
		
		PATH.set( path );
	}
	
	public static boolean useDimensionChannels() {
		
		return USE_DIMENSION_CHANNELS.get();
	}
	
	public static void setUseDimensionChannels( boolean useDimensionChannels ) {
		
		USE_DIMENSION_CHANNELS.set( useDimensionChannels );
	}
}
