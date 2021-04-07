package de.geheimagentnr1.mumbleintegration.config.gui;

import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import de.geheimagentnr1.mumbleintegration.config.gui.config.OptionsListWidget;
import de.geheimagentnr1.mumbleintegration.config.gui.config.value.OptionsEntryValueBoolean;
import de.geheimagentnr1.mumbleintegration.config.gui.config.value.OptionsEntryValueInteger;
import de.geheimagentnr1.mumbleintegration.config.gui.config.value.OptionsEntryValueString;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.Objects;


public class ModGuiConfig extends GuiOptions {
	
	
	public ModGuiConfig( @Nonnull Screen parent ) {
		
		super( parent, new StringTextComponent( ClientConfig.getModName() ) );
	}
	
	@Nonnull
	@Override
	public OptionsListWidget getOptions() {
		
		Objects.requireNonNull( minecraft );
		OptionsListWidget options = new OptionsListWidget( this, minecraft, width + 45, height, 32, height - 32, 30 );
		options.add( new OptionsEntryValueBoolean(
			"Mumble Active",
			"Should the mumble integration be activ?",
			ClientConfig.isMumbleActive(),
			ClientConfig::setMumbleActive
		) );
		options.add( new OptionsEntryValueBoolean(
			"Auto Connect",
			"Should mumble be started automated?",
			ClientConfig.shouldAutoConnect(),
			ClientConfig::setAutoConnect
		) );
		options.add( new OptionsEntryValueString(
			"Address",
			"Address of the mumble server.",
			ClientConfig.getAddress(),
			ClientConfig::setAddress
		) );
		options.add( new OptionsEntryValueInteger(
			"Port",
			"Port of the mumble server.",
			ClientConfig.getPort(),
			ClientConfig::setPort
		) );
		options.add( new OptionsEntryValueString(
			"Path",
			"Path of the mumble channel.",
			ClientConfig.getPath(),
			ClientConfig::setPath
		) );
		options.add( new OptionsEntryValueBoolean(
			"Use Dimension Channels",
			"Use subchannels for each dimension?",
			ClientConfig.useDimensionChannels(),
			ClientConfig::setUseDimensionChannels
		) );
		return options;
	}
}
