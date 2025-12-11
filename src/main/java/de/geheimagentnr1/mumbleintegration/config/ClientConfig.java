package de.geheimagentnr1.mumbleintegration.config;

import de.geheimagentnr1.mumbleintegration.MumbleIntegration;
import de.geheimagentnr1.mumbleintegration.config.gui.ModConfigScreen;
import de.geheimagentnr1.mumbleintegration.linking.MumbleLinker;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.jetbrains.annotations.NotNull;


public class ClientConfig {
	
	
	@NotNull
	private final ModConfigSpec spec;
	
	@NotNull
	private final ModConfigSpec.BooleanValue mumbleActive;
	
	@NotNull
	private final ModConfigSpec.BooleanValue autoConnect;
	
	@NotNull
	private final ModConfigSpec.ConfigValue<String> address;
	
	@NotNull
	private final ModConfigSpec.IntValue port;
	
	@NotNull
	private final ModConfigSpec.ConfigValue<String> path;
	
	@NotNull
	private final ModConfigSpec.BooleanValue useDimensionChannels;
	
	@NotNull
	private final MumbleLinker mumbleLinker;
	
	public ClientConfig( @NotNull MumbleLinker _mumbleLinker ) {
		
		mumbleLinker = _mumbleLinker;
		
		ModConfigSpec.Builder builder = new ModConfigSpec.Builder();
		
		mumbleActive = builder
			.comment( "Should the Mumble integration be active?" )
			.define( "mumble_active", false );
		
		autoConnect = builder
			.comment( "Should Mumble be connect automated?" )
			.define( "auto_connect", false );
		
		address = builder
			.comment( "Address of the Mumble server." )
			.define( "address", "" );
		
		port = builder
			.comment( "Port of the Mumble server." )
			.defineInRange( "port", 64738, 0, 65535 );
		
		path = builder
			.comment( "Path of the Mumble channel." )
			.define( "path", "" );
		
		useDimensionChannels = builder
			.comment( "Use subchannels for each dimension?" )
			.define( "use_dimension_channels", false );
		
		spec = builder.build();
	}
	
	@NotNull
	public ModConfigSpec getSpec() {
		
		return spec;
	}
	
	public void registerConfigScreen() {
		
		ModList.get().getModContainerById( MumbleIntegration.MODID ).ifPresent( ( ModContainer modContainer ) ->
			modContainer.registerExtensionPoint( IConfigScreenFactory.class, ( container, screen ) ->
				new ModConfigScreen( this, screen )
			)
		);
	}
	
	public void handleConfigChange() {
		
		if( isMumbleActive() ) {
			mumbleLinker.link();
		} else {
			mumbleLinker.unlink();
		}
	}
	
	public boolean isMumbleActive() {
		
		return mumbleActive.get();
	}
	
	public void setMumbleActive( boolean value ) {
		
		mumbleActive.set( value );
		handleConfigChange();
	}
	
	public boolean shouldAutoConnect() {
		
		return autoConnect.get();
	}
	
	public void setAutoConnect( boolean value ) {
		
		autoConnect.set( value );
	}
	
	@NotNull
	public String getAddress() {
		
		return address.get();
	}
	
	public void setAddress( @NotNull String value ) {
		
		address.set( value );
	}
	
	public int getPort() {
		
		return port.get();
	}
	
	public void setPort( int value ) {
		
		port.set( value );
	}
	
	@NotNull
	public String getPath() {
		
		return path.get();
	}
	
	public void setPath( @NotNull String value ) {
		
		path.set( value );
	}
	
	public boolean useDimensionChannels() {
		
		return useDimensionChannels.get();
	}
	
	public void setUseDimensionChannels( boolean value ) {
		
		useDimensionChannels.set( value );
	}
}
