package de.geheimagentnr1.mumbleintegration.config.gui.list.values;

import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.regex.Pattern;


public class IntegerConfigEntry extends InputConfigEntry<Integer> {
	
	
	@Nonnull
	private static final Pattern INT_PATTERN = Pattern.compile( "[0-9]*" );
	
	public IntegerConfigEntry(
		@Nonnull Minecraft _minecraft,
		@Nonnull String title,
		@Nonnull String description,
		@Nonnull Integer value,
		@Nonnull Consumer<Integer> saver ) {
		
		super( _minecraft, title, description, value, saver, text -> INT_PATTERN.matcher( text ).matches() );
	}
	
	//package-private
	@Override
	void setInputValue( @Nonnull String text ) {
		
		try {
			setValue( Integer.valueOf( text ) );
		} catch( NumberFormatException ignored ) {
			setValue( 0 );
		}
	}
}
