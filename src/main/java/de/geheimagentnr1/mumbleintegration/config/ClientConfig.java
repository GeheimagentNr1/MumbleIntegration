package de.geheimagentnr1.mumbleintegration.config;

import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import de.geheimagentnr1.minecraft_forge_api.config.AbstractConfig;
import de.geheimagentnr1.mumbleintegration.config.gui.ModConfigScreen;
import de.geheimagentnr1.mumbleintegration.linking.MumbleLinker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.jetbrains.annotations.NotNull;


public class ClientConfig extends AbstractConfig {
	
	
	@NotNull
	private static final String MUMBLE_ACTIVE_KEY = "mumble_active";
	
	@NotNull
	private static final String AUTO_CONNECT_KEY = "auto_connect";
	
	@NotNull
	private static final String ADDRESS_KEY = "address";
	
	@NotNull
	private static final String PORT_KEY = "port";
	
	@NotNull
	private static final String PATH_KEY = "path";
	
	@NotNull
	private static final String USE_DIMENSION_CHANNELS_KEY = "use_dimension_channels";
	
	@NotNull
	private final MumbleLinker mumbleLinker;
	
	public ClientConfig( @NotNull AbstractMod _abstractMod, @NotNull MumbleLinker _mumbleLinker ) {
		
		super( _abstractMod );
		mumbleLinker = _mumbleLinker;
	}
	
	@NotNull
	@Override
	public ModConfig.Type type() {
		
		return ModConfig.Type.CLIENT;
	}
	
	@Override
	public boolean isEarlyLoad() {
		
		return false;
	}
	
	@Override
	protected void registerConfigValues() {
		
		registerConfigValue( "Should the Mumble integration be active?", MUMBLE_ACTIVE_KEY, false );
		registerConfigValue( "Should Mumble be connect automated?", AUTO_CONNECT_KEY, false );
		registerConfigValue( "Address of the Mumble server.", ADDRESS_KEY, "" );
		registerConfigValue(
			"Port of the Mumble server.",
			PORT_KEY,
			( builder, path ) -> builder.defineInRange( path, 64738, 0, 65535 )
		);
		registerConfigValue( "Path of the Mumble channel.", PATH_KEY, "" );
		registerConfigValue( "Use subchannels for each dimension?", USE_DIMENSION_CHANNELS_KEY, false );
	}
	
	@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	@Override
	public void handleFMLClientSetupEvent( @NotNull FMLClientSetupEvent event ) {
		
		ModLoadingContext.get().registerExtensionPoint(
			ConfigScreenHandler.ConfigScreenFactory.class,
			() -> new ConfigScreenHandler.ConfigScreenFactory(
				( minecraft, screen ) -> new ModConfigScreen( abstractMod, this, screen )
			)
		);
	}
	
	@Override
	protected void handleConfigChanging() {
		
		if( isMumbleActive() ) {
			mumbleLinker.link();
		} else {
			mumbleLinker.unlink();
		}
	}
	
	public boolean isMumbleActive() {
		
		return getValue( Boolean.class, MUMBLE_ACTIVE_KEY );
	}
	
	public void setMumbleActive( boolean mumbleActive ) {
		
		setValue( Boolean.class, MUMBLE_ACTIVE_KEY, mumbleActive );
	}
	
	public boolean shouldAutoConnect() {
		
		return getValue( Boolean.class, AUTO_CONNECT_KEY );
	}
	
	public void setAutoConnect( boolean autoConnect ) {
		
		setValue( Boolean.class, AUTO_CONNECT_KEY, autoConnect );
	}
	
	@NotNull
	public String getAddress() {
		
		return getValue( String.class, ADDRESS_KEY );
	}
	
	public void setAddress( @NotNull String address ) {
		
		setValue( String.class, ADDRESS_KEY, address );
	}
	
	public int getPort() {
		
		return getValue( Integer.class, PORT_KEY );
	}
	
	public void setPort( int port ) {
		
		setValue( Integer.class, PORT_KEY, port );
	}
	
	@NotNull
	public String getPath() {
		
		return getValue( String.class, PATH_KEY );
	}
	
	public void setPath( @NotNull String path ) {
		
		setValue( String.class, PATH_KEY, path );
	}
	
	public boolean useDimensionChannels() {
		
		return getValue( Boolean.class, USE_DIMENSION_CHANNELS_KEY );
	}
	
	public void setUseDimensionChannels( boolean useDimensionChannels ) {
		
		setValue( Boolean.class, USE_DIMENSION_CHANNELS_KEY, useDimensionChannels );
	}
}
