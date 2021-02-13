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
	
	//package-private
	@Nonnull
	T value;
	
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
		
		client.fontRenderer.drawStringWithShadow( title.getFormattedText(),
		                                          rowLeft + 10,
		                                          rowTop + (float)( height / 4 + client.fontRenderer.FONT_HEIGHT / 2 ),
		                                          16777215
		);
		drawValue( height, rowLeft, rowTop, mouseX, mouseY, deltaTime );
		x = rowLeft;
		y = rowTop;
	}
	
	public void save() {
		
		save.accept( value );
	}
	
	@Nonnull
	public abstract IGuiEventListener getListener();
	
	@Nonnull
	public TextComponent getTitle() {
		
		return title;
	}
	
	@Nonnull
	public String getDescription() {
		
		return description;
	}
	
	public int getX() {
		
		return x;
	}
	
	public int getY() {
		
		return y;
	}
	
	protected abstract void drawValue( int entryHeight, int _x, int _y, int mouseX, int mouseY, float partialTicks );
}
