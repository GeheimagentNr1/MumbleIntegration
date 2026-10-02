package de.geheimagentnr1.mumbleintegration.config.gui;

import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;


public class ModConfigScreen extends Screen {
	
	
	@NotNull
	private final ClientConfig config;
	
	@NotNull
	private final Screen parent;
	
	private EditBox addressField;
	private EditBox portField;
	private EditBox pathField;
	
	private boolean mumbleActive;
	private boolean autoConnect;
	private boolean useDimensionChannels;
	
	public ModConfigScreen( @NotNull ClientConfig _config, @NotNull Screen _parent ) {
		
		super( Component.literal( "Mumble Integration Config" ) );
		config = _config;
		parent = _parent;
	}
	
	@Override
	protected void init() {
		
		mumbleActive = config.isMumbleActive();
		autoConnect = config.shouldAutoConnect();
		useDimensionChannels = config.useDimensionChannels();
		
		int centerX = this.width / 2;
		int y = 40;
		int buttonWidth = 200;
		int buttonHeight = 20;
		int spacing = 24;
		
		// Mumble Active Toggle
		this.addRenderableWidget( Button.builder(
			Component.literal( "Mumble Active: " + ( mumbleActive ? "ON" : "OFF" ) ),
			button -> {
				mumbleActive = !mumbleActive;
				button.setMessage( Component.literal( "Mumble Active: " + ( mumbleActive ? "ON" : "OFF" ) ) );
			}
		).bounds( centerX - buttonWidth / 2, y, buttonWidth, buttonHeight ).build() );
		y += spacing;
		
		// Auto Connect Toggle
		this.addRenderableWidget( Button.builder(
			Component.literal( "Auto Connect: " + ( autoConnect ? "ON" : "OFF" ) ),
			button -> {
				autoConnect = !autoConnect;
				button.setMessage( Component.literal( "Auto Connect: " + ( autoConnect ? "ON" : "OFF" ) ) );
			}
		).bounds( centerX - buttonWidth / 2, y, buttonWidth, buttonHeight ).build() );
		y += spacing;
		
		// Address Field
		addressField = new EditBox( this.font, centerX - buttonWidth / 2, y, buttonWidth, buttonHeight, Component.literal( "Address" ) );
		addressField.setMaxLength( 256 );
		addressField.setValue( config.getAddress() );
		this.addRenderableWidget( addressField );
		y += spacing;
		
		// Port Field
		portField = new EditBox( this.font, centerX - buttonWidth / 2, y, buttonWidth, buttonHeight, Component.literal( "Port" ) );
		portField.setMaxLength( 5 );
		portField.setValue( String.valueOf( config.getPort() ) );
		//EditBox.setFilter was removed in 26.2: remove non-digits right after the change (before rendering)
		portField.setResponder( value -> {
			String digits = value.replaceAll( "\\D", "" );
			if( !digits.equals( value ) ) {
				portField.setValue( digits );
			}
		} );
		this.addRenderableWidget( portField );
		y += spacing;
		
		// Path Field
		pathField = new EditBox( this.font, centerX - buttonWidth / 2, y, buttonWidth, buttonHeight, Component.literal( "Path" ) );
		pathField.setMaxLength( 256 );
		pathField.setValue( config.getPath() );
		this.addRenderableWidget( pathField );
		y += spacing;
		
		// Use Dimension Channels Toggle
		this.addRenderableWidget( Button.builder(
			Component.literal( "Use Dimension Channels: " + ( useDimensionChannels ? "ON" : "OFF" ) ),
			button -> {
				useDimensionChannels = !useDimensionChannels;
				button.setMessage( Component.literal( "Use Dimension Channels: " + ( useDimensionChannels ? "ON" : "OFF" ) ) );
			}
		).bounds( centerX - buttonWidth / 2, y, buttonWidth, buttonHeight ).build() );
		y += spacing + 10;
		
		// Done Button
		this.addRenderableWidget( Button.builder(
			CommonComponents.GUI_DONE,
			button -> this.onClose()
		).bounds( centerX - buttonWidth / 2, y, buttonWidth, buttonHeight ).build() );
	}
	
	@Override
	public void extractRenderState( @NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick ) {
		
		super.extractRenderState( graphics, mouseX, mouseY, partialTick );
		graphics.centeredText( this.font, this.title, this.width / 2, 15, 0xFFFFFFFF );
		
		int centerX = this.width / 2;
		int labelX = centerX - 100 - 5;
		int y = 40;
		int spacing = 24;
		
		y += spacing * 2; // Skip first two buttons
		drawLabel( graphics, "Address:", labelX, y + 6 );
		y += spacing;
		drawLabel( graphics, "Port:", labelX, y + 6 );
		y += spacing;
		drawLabel( graphics, "Path:", labelX, y + 6 );
	}
	
	//Right-aligned label ending at endX
	private void drawLabel( @NotNull GuiGraphicsExtractor graphics, @NotNull String label, int endX, int y ) {
		
		graphics.text( this.font, label, endX - this.font.width( label ), y, 0xFFA0A0A0 );
	}
	
	@Override
	public void onClose() {
		
		saveConfig();
		this.minecraft.gui.setScreen( parent );
	}
	
	private void saveConfig() {
		
		config.setMumbleActive( mumbleActive );
		config.setAutoConnect( autoConnect );
		config.setAddress( addressField.getValue() );
		
		try {
			int port = Integer.parseInt( portField.getValue() );
			if( port >= 0 && port <= 65535 ) {
				config.setPort( port );
			}
		} catch( NumberFormatException ignored ) {
		}
		
		config.setPath( pathField.getValue() );
		config.setUseDimensionChannels( useDimensionChannels );
	}
}
