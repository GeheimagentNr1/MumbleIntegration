package de.geheimagentnr1.mumbleintegration.config.gui.config.value;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;


//package-private
class WatchedTextField extends TextFieldWidget {
	
	
	@Nonnull
	private final OptionsEntryValueInput<?> watcher;
	
	//package-private
	@SuppressWarnings( "SameParameterValue" )
	WatchedTextField(
		@Nonnull FontRenderer fontIn,
		int xIn,
		int yIn,
		int widthIn,
		int heightIn,
		@Nonnull OptionsEntryValueInput<?> _watcher ) {
		
		super( fontIn, xIn, yIn, widthIn, heightIn, new StringTextComponent( "" ) );
		watcher = _watcher;
		initResponder();
	}
	
	private void initResponder() {
		
		setResponder( watcher::setValue );
	}
}
