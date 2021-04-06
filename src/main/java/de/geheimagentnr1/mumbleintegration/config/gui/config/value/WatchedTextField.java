package de.geheimagentnr1.mumbleintegration.config.gui.config.value;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;

import javax.annotation.Nonnull;


//package-private
class WatchedTextField extends TextFieldWidget {
	
	
	@Nonnull
	private final OptionsEntryValueInput<?> watcher;
	
	//package-private
	WatchedTextField(
		@Nonnull OptionsEntryValueInput<?> _watcher,
		@Nonnull FontRenderer fontRenderer,
		int _x,
		int _y,
		int _width,
		int _height ) {
		
		super( fontRenderer, _x, _y, _width, _height, "" );
		watcher = _watcher;
	}
	
	@Override
	public void writeText( @Nonnull String textToWrite ) {
		
		super.writeText( textToWrite );
		watcher.setInputValue( getText() );
	}
	
	@Override
	public void setText( @Nonnull String textIn ) {
		
		super.setText( textIn );
		watcher.setInputValue( getText() );
	}
	
	@Override
	public void deleteWords( int num ) {
		
		super.deleteWords( num );
		watcher.setInputValue( getText() );
	}
}
