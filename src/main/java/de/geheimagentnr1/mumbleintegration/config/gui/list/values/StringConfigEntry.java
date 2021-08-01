package de.geheimagentnr1.mumbleintegration.config.gui.list.values;

import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.function.Consumer;


public class StringConfigEntry extends InputConfigEntry<String> {
	
	
	public StringConfigEntry(
		@Nonnull Minecraft _minecraft,
		@Nonnull String title,
		@Nonnull String description,
		@Nonnull String value,
		@Nonnull Consumer<String> saver ) {
		
		super( _minecraft, title, description, value, saver, text -> true );
	}
	
	//package-private
	@Override
	void setInputValue( @Nonnull String text ) {
		
		setValue( text );
	}
}
