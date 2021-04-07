package de.geheimagentnr1.mumbleintegration.config.gui.config.value;

import de.geheimagentnr1.mumbleintegration.config.gui.config.OptionsListWidgetEntry;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;

import javax.annotation.Nonnull;
import java.util.function.Consumer;


public abstract class OptionsEntryValue<T> extends OptionsListWidgetEntry {
	
	
	@Nonnull
	private final TextComponent title;
	
	@Nonnull
	private final String description;
	
	@Nonnull
	private final Consumer<T> save;
	
	@Nonnull
	private T value;
	
	private int x;
	
	private int y;
	
	//package-private
	OptionsEntryValue(
		@Nonnull String optionName,
		@Nonnull String _description,
		@Nonnull T _value,
		@Nonnull Consumer<T> _save ) {
		
		title = new StringTextComponent( optionName );
		description = _description;
		value = _value;
		save = _save;
	}
	
	@Override
	public void render(
		int index,
		int rowTop,
		int rowLeft,
		int width,
		int height,
		int mouseX,
		int mouseY,
		boolean hovered,
		float deltaTime ) {
		
		minecraft.fontRenderer.drawStringWithShadow(
			title.getFormattedText(),
			rowLeft + 10,
			rowTop + (float)( height / 4 + minecraft.fontRenderer.FONT_HEIGHT / 2 ),
			16777215
		);
		drawValue( height, rowLeft, rowTop, mouseX, mouseY, deltaTime );
		x = rowLeft;
		y = rowTop;
	}
	
	@Nonnull
	public TextComponent getTitle() {
		
		return title;
	}
	
	@Nonnull
	public String getDescription() {
		
		return description;
	}
	
	protected abstract void drawValue( int _height, int _x, int _y, int mouseX, int mouseY, float partialTicks );
	
	@Nonnull
	public abstract IGuiEventListener getListener();
	
	public void save() {
		
		save.accept( value );
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
	
	public int getX() {
		
		return x;
	}
	
	public int getY() {
		
		return y;
	}
}
