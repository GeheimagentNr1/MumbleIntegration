package de.geheimagentnr1.mumbleintegration.config.gui.config.value;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;

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
		
		super( fontIn, xIn, yIn, widthIn, heightIn, "" );
		watcher = _watcher;
	}
	
	@Override
	public void writeText( @Nonnull String textToWrite ) {
		
		super.writeText( textToWrite );
		watcher.setValue( getText() );
	}
	
	@Override
	public void setText( @Nonnull String textIn ) {
		
		super.setText( textIn );
		watcher.setValue( getText() );
	}
	
	@Override
	public void deleteWords( int num ) {
		
		super.deleteWords( num );
		watcher.setValue( getText() );
	}
}
