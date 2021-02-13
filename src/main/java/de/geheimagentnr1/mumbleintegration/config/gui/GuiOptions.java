package de.geheimagentnr1.mumbleintegration.config.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import de.geheimagentnr1.mumbleintegration.config.gui.config.OptionsListWidget;
import de.geheimagentnr1.mumbleintegration.config.gui.config.value.OptionsEntryValue;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextProperties;
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
	public void func_231158_b_( @Nonnull Minecraft client, int _width, int _height ) {
		
		super.func_231158_b_( client, _width, _height );
		
		options = getOptions();
		field_230705_e_.add( options );
		func_231035_a_( options );
		
		func_230480_a_( new Button(
			_width / 2 - 100,
			_height - 25,
			100,
			20,
			new TranslationTextComponent( "gui.done" ),
			w -> {
				options.save();
				func_231175_as__();
			}
		) );
		func_230480_a_( new Button(
			_width / 2 + 5,
			_height - 25,
			100,
			20,
			new TranslationTextComponent( "gui.cancel" ),
			w -> func_231175_as__()
		) );
	}
	
	@Override
	public void func_230430_a_( @Nonnull MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks ) {
		
		Objects.requireNonNull( options );
		func_230446_a_( matrixStack );
		options.func_230430_a_( matrixStack, mouseX, mouseY, partialTicks );
		func_238471_a_( matrixStack, field_230712_o_, field_230704_d_.getString(), field_230708_k_ / 2, 12, 16777215 );
		super.func_230430_a_( matrixStack, mouseX, mouseY, partialTicks );
		if( mouseY < 32 || mouseY > field_230709_l_ - 32 ) {
			return;
		}
		options.forEach( entry -> {
			if( entry instanceof OptionsEntryValue ) {
				OptionsEntryValue<?> value = (OptionsEntryValue<?>)entry;
				
				int valueX = value.getX() + 10;
				int valueY = value.getY() + 10;
				String formatted_title = value.getTitle().getString();
				if( mouseX < valueX || mouseX > valueX + field_230712_o_.getStringWidth( formatted_title ) ||
					mouseY < valueY || mouseY > valueY + 9 ) {
					return;
				}
				List<ITextProperties> tooltip = Lists.newArrayList();
				tooltip.addAll( field_230712_o_.func_238425_b_(
					new StringTextComponent( value.getDescription() ),
					200
				) );
				func_238654_b_( matrixStack, tooltip, mouseX, mouseY );
			}
		} );
	}
	
	@Override
	public void func_231175_as__() {
		
		Objects.requireNonNull( field_230706_i_ ).displayGuiScreen( parent );
	}
	
	public void addListener( @Nonnull IGuiEventListener listener ) {
		
		field_230705_e_.add( listener );
	}
	
	@Nonnull
	protected abstract OptionsListWidget getOptions();
}
