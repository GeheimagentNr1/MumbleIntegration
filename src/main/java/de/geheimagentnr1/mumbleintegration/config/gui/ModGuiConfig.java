package de.geheimagentnr1.mumbleintegration.config.gui;

import de.geheimagentnr1.mumbleintegration.config.MainConfig;
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
		
		super( parent, new StringTextComponent( MainConfig.mod_name ) );
	}
	
	@Nonnull
	@Override
	public OptionsListWidget getOptions() {
		
		OptionsListWidget options = new OptionsListWidget(
			this,
			Objects.requireNonNull( field_230706_i_ ),
			field_230708_k_ + 45, field_230709_l_,
			32,
			field_230709_l_ - 32,
			30
		);
		options.add( new OptionsEntryValueBoolean(
			"Mumble Active",
			"Should the mumble integration be activ?",
			MainConfig.MUMBLE_ACTIVE.get(),
			MainConfig.MUMBLE_ACTIVE::set
		) );
		options.add( new OptionsEntryValueBoolean(
			"Auto Connect",
			"Should mumble be started automated?",
			MainConfig.AUTO_CONNECT.get(),
			MainConfig.AUTO_CONNECT::set
		) );
		options.add( new OptionsEntryValueString(
			"Address",
			"Address of the mumble server.",
			MainConfig.ADDRESS.get(),
			MainConfig.ADDRESS::set
		) );
		options.add( new OptionsEntryValueInteger(
			"Port",
			"Port of the mumble server.",
			MainConfig.PORT.get(),
			MainConfig.PORT::set
		) );
		options.add( new OptionsEntryValueString(
			"Path",
			"Path of the mumble channel.",
			MainConfig.PATH.get(),
			MainConfig.PATH::set
		) );
		options.add( new OptionsEntryValueBoolean(
			"Use Dimension Channels",
			"Use subchannels for each dimension?",
			MainConfig.USE_DIMENSION_CHANNELS.get(),
			MainConfig.USE_DIMENSION_CHANNELS::set
		) );
		return options;
	}
}
