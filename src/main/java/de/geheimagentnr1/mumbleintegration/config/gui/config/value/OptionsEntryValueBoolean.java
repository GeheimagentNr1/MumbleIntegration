package de.geheimagentnr1.mumbleintegration.config.gui.config.value;

import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.button.Button;

import javax.annotation.Nonnull;
import java.util.function.Consumer;


public class OptionsEntryValueBoolean extends OptionsEntryValue<Boolean> {
	
	
	@Nonnull
	private final Button button;
	
	public OptionsEntryValueBoolean( @Nonnull String optionName, @Nonnull String _description, boolean _value,
		@Nonnull Consumer<Boolean> _save ) {
		
		super( optionName, _description, _value, _save );
		button = new Button( 0, 0, 100, 20, String.valueOf( value ), w -> value = !value );
	}
	
	@Override
	protected void drawValue( int entryHeight, int _x, int _y, int mouseX, int mouseY, float partialTicks ) {
		
		button.x = _x + 135;
		button.y = _y + entryHeight / 6;
		button.setMessage( String.valueOf( value ) );
		button.render( mouseX, mouseY, partialTicks );
	}
	
	@Nonnull
	@Override
	public IGuiEventListener getListener() {
		
		return button;
	}
}
