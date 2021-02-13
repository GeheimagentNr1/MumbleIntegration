package de.geheimagentnr1.mumbleintegration.config.gui.config.value;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;


public class OptionsEntryValueBoolean extends OptionsEntryValue<Boolean> {
	
	
	@Nonnull
	private final Button button;
	
	public OptionsEntryValueBoolean(
		@Nonnull String optionName,
		@Nonnull String _description,
		boolean _value,
		@Nonnull Consumer<Boolean> _save ) {
		
		super( optionName, _description, _value, _save );
		button = new Button( 0, 0, 100, 20, new StringTextComponent( String.valueOf( value ) ), w -> value = !value );
	}
	
	@Override
	protected void drawValue( @Nonnull MatrixStack matrixStack, int entryHeight, int _x, int _y, int mouseX,
		int mouseY,
		float partialTicks ) {
		
		button.field_230690_l_ = _x + 135;
		button.field_230691_m_ = _y + entryHeight / 6;
		button.func_238482_a_( new StringTextComponent( String.valueOf( value ) ) );
		button.func_230430_a_( matrixStack, mouseX, mouseY, partialTicks );
	}
	
	@Nonnull
	@Override
	public IGuiEventListener getListener() {
		
		return button;
	}
	
	@Nonnull
	@Override
	public List<? extends IGuiEventListener> func_231039_at__() {
		
		return ImmutableList.of();
	}
}
