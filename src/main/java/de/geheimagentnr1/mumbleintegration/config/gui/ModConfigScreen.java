package de.geheimagentnr1.mumbleintegration.config.gui;

import com.google.common.collect.ImmutableList;
import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import de.geheimagentnr1.mumbleintegration.config.gui.list.ConfigEntry;
import de.geheimagentnr1.mumbleintegration.config.gui.list.ConfigList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class ModConfigScreen extends Screen {
	
	
	private final Screen parent;
	
	private ConfigList configList;
	
	public ModConfigScreen( Screen _parent ) {
		
		super( Component.literal( ClientConfig.getModName() ) );
		parent = _parent;
	}
	
	@Override
	protected void init() {
		
		configList = new ConfigList( minecraft, width, height, 32, height - 32, 25 );
		addWidget( configList );
		addRenderableWidget(
			Button.builder(
					Component.translatable( "gui.done" ),
					w -> {
						configList.save();
						onClose();
					}
				)
				.pos( width / 2 - 100, height - 25 )
				.size( 100, 20 )
				.build()
		);
		addRenderableWidget(
			Button.builder( Component.translatable( "gui.cancel" ), w -> onClose() )
				.pos( width / 2 + 5, height - 25 )
				.size( 100, 20 )
				.build()
		);
	}
	
	@Override
	public void render( @Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick ) {
		
		renderBackground( guiGraphics );
		configList.render( guiGraphics, mouseX, mouseY, partialTick );
		guiGraphics.drawCenteredString( font, title, width / 2, 5, 16777215 );
		super.render( guiGraphics, mouseX, mouseY, partialTick );
		List<FormattedCharSequence> list = tooltipAt( mouseX, mouseY );
		if( list != null ) {
			guiGraphics.renderTooltip( font, list, mouseX, mouseY );
		}
	}
	
	private List<FormattedCharSequence> tooltipAt( int mouseX, int mouseY ) {
		
		Optional<ConfigEntry> optional = configList.getMouseOver( mouseX, mouseY );
		return optional.map( ConfigEntry::getTooltip ).orElseGet( ImmutableList::of );
	}
	
	@Override
	public void onClose() {
		
		Objects.requireNonNull( minecraft );
		minecraft.setScreen( parent );
	}
}
