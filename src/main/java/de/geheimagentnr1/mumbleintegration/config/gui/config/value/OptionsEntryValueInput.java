package de.geheimagentnr1.mumbleintegration.config.gui.config.value;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.TextFieldWidget;

import javax.annotation.Nonnull;
import java.util.List;
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
		textField = new WatchedTextField( client.fontRenderer, 0, 0, 98, 18, this );
		textField.setText( String.valueOf( value ) );
		textField.setValidator( validator );
	}
	
	@Override
	protected void drawValue( @Nonnull MatrixStack matrixStack, int entryHeight, int _x, int _y, int mouseX,
		int mouseY,
		float partialTicks ) {
		
		textField.field_230690_l_ = _x + 135;
		textField.field_230691_m_ = _y + entryHeight / 6;
		textField.func_230430_a_( matrixStack, mouseX, mouseY, partialTicks );
	}
	
	@Nonnull
	@Override
	public IGuiEventListener getListener() {
		
		return textField;
	}
	
	//package-private
	abstract void setValue( @Nonnull String text );
	
	@Nonnull
	@Override
	public List<? extends IGuiEventListener> func_231039_at__() {
		
		return ImmutableList.of();
	}
}
