package de.geheimagentnr1.mumbleintegration.config.gui.list.values;

import de.geheimagentnr1.mumbleintegration.config.gui.list.ConfigEntry;
import net.minecraft.client.Minecraft;

import javax.annotation.Nonnull;
import java.util.function.Consumer;


public abstract class ValueConfigEntry<T> extends ConfigEntry {
	
	
	@Nonnull
	private T value;
	
	@Nonnull
	private final Consumer<T> saver;
	
	
	//package-private
	ValueConfigEntry(
		@Nonnull Minecraft _minecraft,
		@Nonnull String _title,
		@Nonnull String _description,
		@Nonnull T _value,
		@Nonnull Consumer<T> _saver ) {
		
		super( _minecraft, _title, _description );
		value = _value;
		saver = _saver;
	}
	
	public void save() {
		
		saver.accept( value );
	}
	
	//package-private
	@Nonnull
	final T getValue() {
		
		return value;
	}
	
	//package-private
	void setValue( @Nonnull T _value ) {
		
		value = _value;
	}
}
