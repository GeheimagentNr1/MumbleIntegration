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
		button = new Button(
			0,
			0,
			100,
			20,
			buildShownText(),
			pressedButton -> {
				setValue( !getValue() );
				pressedButton.setMessage( buildShownText() );
			}
		);
	}
	
	@Override
	protected void drawValue(
		@Nonnull MatrixStack matrixStack,
		int _height,
		int _x,
		int _y,
		int mouseX,
		int mouseY,
		float partialTicks ) {
		
		button.x = _x + 135;
		button.y = _y + _height / 6;
		button.render( matrixStack, mouseX, mouseY, partialTicks );
	}
	
	private StringTextComponent buildShownText() {
		
		return new StringTextComponent( getValue() ? "Yes" : "No" );
	}
	
	@Nonnull
	@Override
	public IGuiEventListener getParentListener() {
		
		return button;
	}
	
	@Nonnull
	@Override
	public List<? extends IGuiEventListener> children() {
		
		return ImmutableList.of();
	}
}
