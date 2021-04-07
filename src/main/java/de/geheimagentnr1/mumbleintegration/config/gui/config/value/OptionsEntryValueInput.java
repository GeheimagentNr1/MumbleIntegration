package de.geheimagentnr1.mumbleintegration.config.gui.config.value;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.TextFieldWidget;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Predicate;


public abstract class OptionsEntryValueInput<T> extends OptionsEntryValue<T> {
	
	
	@Nonnull
	private final TextFieldWidget textField;
	
	//package-private
	@SuppressWarnings( "ThisEscapedInObjectConstruction" )
	OptionsEntryValueInput(
		@Nonnull String optionName,
		@Nonnull String _description,
		@Nonnull T _value,
		@Nonnull Predicate<String> validator,
		@Nonnull Consumer<T> _save ) {
		
		super( optionName, _description, _value, _save );
		textField = new WatchedTextField( minecraft.fontRenderer, 0, 0, 98, 18, this );
		textField.setText( String.valueOf( getValue() ) );
		textField.setValidator( validator );
	}
	
	@Override
	protected void drawValue( int _height, int _x, int _y, int mouseX, int mouseY, float partialTicks ) {
		
		textField.x = _x + 135;
		textField.y = _y + _height / 6;
		textField.render( mouseX, mouseY, partialTicks );
	}
	
	@Nonnull
	@Override
	public IGuiEventListener getListener() {
		
		return textField;
	}
	
	//package-private
	abstract void setInputValue( @Nonnull String text );
}
