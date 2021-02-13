package de.geheimagentnr1.mumbleintegration.config.gui.config.value;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.regex.Pattern;


public class OptionsEntryValueInteger extends OptionsEntryValueInput<Integer> {
	
	
	@Nonnull
	private static final Pattern INT_PATTERN = Pattern.compile( "[0-9]*" );
	
	public OptionsEntryValueInteger(
		@Nonnull String optionName, @Nonnull String _description, int _value, @Nonnull Consumer<Integer> _save ) {
		
		super( optionName, _description, _value, text -> INT_PATTERN.matcher( text ).matches(), _save );
	}
	
	//package-private
	@Override
	void setValue( @Nonnull String text ) {
		
		try {
			value = Integer.valueOf( text );
		} catch( NumberFormatException ignored ) {
			value = 0;
		}
	}
}
