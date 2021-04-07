package de.geheimagentnr1.mumbleintegration.config.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.geheimagentnr1.mumbleintegration.config.gui.config.OptionsListWidget;
import de.geheimagentnr1.mumbleintegration.config.gui.config.value.OptionsEntryValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextComponent;
import net.minecraft.util.text.TranslationTextComponent;

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
	public void init( @Nonnull Minecraft _minecraft, int width, int height ) {
		
		super.init( _minecraft, width, height );
		
		options = getOptions();
		children.add( options );
		setListener( options );
		
		addButton( new Button(
			width / 2 - 100,
			height - 25, 100, 20, new TranslationTextComponent( "gui.done" ),
			w -> {
				options.save();
				closeScreen();
			}
		) );
		addButton( new Button(
			width / 2 + 5,
			height - 25, 100, 20, new TranslationTextComponent( "gui.cancel" ),
			w -> closeScreen()
		) );
	}
	
	@Override
	public void render( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {
		
		renderBackground( matrixStack );
		Objects.requireNonNull( options );
		options.render( matrixStack, mouseX, mouseY, partialTicks );
		drawCenteredString( matrixStack, font, title.getString(), width / 2, 12, 16777215 );
		super.render( matrixStack, mouseX, mouseY, partialTicks );
		if( mouseY < 32 || mouseY > height - 32 ) {
			return;
		}
		options.forEach( entry -> {
			if( entry instanceof OptionsEntryValue ) {
				OptionsEntryValue<?> value = (OptionsEntryValue<?>)entry;
				int valueX = value.getX() + 10;
				int valueY = value.getY() + 10;
				String formatted_title = value.getTitle().getString();
				
				if( mouseX < valueX || mouseX > valueX + font.getStringWidth( formatted_title ) ||
					mouseY < valueY || mouseY > valueY + 9 ) {
					return;
				}
				List<IReorderingProcessor> tooltip = Lists.newArrayList();
				tooltip.addAll( font.trimStringToWidth( new StringTextComponent( value.getDescription() ), 200 ) );
				renderTooltip( matrixStack, tooltip, mouseX, mouseY );
			}
		} );
	}
	
	@Override
	public void closeScreen() {
		
		Objects.requireNonNull( minecraft );
		field_230706_i_.displayGuiScreen( parent );
	}
	
	public void addFromOutsideListener( @Nonnull IGuiEventListener listener ) {
		
		children.add( listener );
	}
	
	@Nonnull
	protected abstract OptionsListWidget getOptions();
}
