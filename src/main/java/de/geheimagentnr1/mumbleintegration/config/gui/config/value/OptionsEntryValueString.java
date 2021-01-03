package de.geheimagentnr1.mumbleintegration.config.gui.config.value;

import javax.annotation.Nonnull;
import java.util.function.Consumer;


public class OptionsEntryValueString extends OptionsEntryValueInput<String> {
	
	
	public OptionsEntryValueString( @Nonnull String optionName, @Nonnull String _description, @Nonnull String _value,
		@Nonnull Consumer<String> _save ) {
		
		super( optionName, _description, _value, text -> true, _save );
	}
	
	//package-private
	@Override
	void setValue( @Nonnull String text ) {
		
		value = text;
	}
}
