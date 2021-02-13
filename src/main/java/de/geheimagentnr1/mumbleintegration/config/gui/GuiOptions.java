package de.geheimagentnr1.mumbleintegration.config.gui;

import com.google.common.collect.Lists;
import de.geheimagentnr1.mumbleintegration.config.gui.config.OptionsListWidget;
import de.geheimagentnr1.mumbleintegration.config.gui.config.value.OptionsEntryValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.TextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;


public abstract class GuiOptions extends Screen {
	
	
	@Nonnull
	private final Screen parent;
	
	@Nullable
	private OptionsListWidget options;
	
	//package-private
	GuiOptions( @Nonnull Screen _parent, @Nonnull TextComponent _title ) {
		
		super( _title );
		parent = _parent;
	}
	
	@Override
	public void init( @Nonnull Minecraft client, int _width, int _height ) {
		
		super.init( client, _width, _height );
		
		options = getOptions();
		children.add( options );
		setFocused( options );
		
		addButton( new Button( _width / 2 - 100, _height - 25, 100, 20, I18n.format( "gui.done" ), button -> {
			options.save();
			onClose();
		} ) );
		addButton( new Button(
			_width / 2 + 5,
			_height - 25,
			100,
			20,
			I18n.format( "gui.cancel" ),
			button -> onClose()
		) );
	}
	
	@Override
	public void render( int mouseX, int mouseY, float partialTicks ) {
		
		Objects.requireNonNull( options );
		renderBackground();
		options.render( mouseX, mouseY, partialTicks );
		drawCenteredString( font, title.getFormattedText(), width / 2, 12, 16777215 );
		super.render( mouseX, mouseY, partialTicks );
		if( mouseY < 32 || mouseY > height - 32 ) {
			return;
		}
		options.forEach( entry -> {
			if( entry instanceof OptionsEntryValue ) {
				OptionsEntryValue<?> value = (OptionsEntryValue<?>)entry;
				
				int valueX = value.getX() + 10;
				int valueY = value.getY() + 10;
				String formatted_title = value.getTitle().getFormattedText();
				if( mouseX < valueX || mouseX > valueX + font.getStringWidth( formatted_title ) || mouseY < valueY ||
					mouseY > valueY + 9 ) {
					return;
				}
				List<String> tooltip = Lists.newArrayList();
				tooltip.addAll( font.listFormattedStringToWidth( value.getDescription(), 200 ) );
				renderTooltip( tooltip, mouseX, mouseY );
			}
		} );
	}
	
	@Override
	public void onClose() {
		
		Objects.requireNonNull( minecraft ).displayGuiScreen( parent );
	}
	
	public void addListener( @Nonnull IGuiEventListener listener ) {
		
		children.add( listener );
	}
	
	@Nonnull
	protected abstract OptionsListWidget getOptions();
}
