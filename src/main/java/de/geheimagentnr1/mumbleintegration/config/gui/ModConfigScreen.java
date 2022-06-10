package de.geheimagentnr1.mumbleintegration.config.gui;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import de.geheimagentnr1.mumbleintegration.config.gui.list.ConfigEntry;
import de.geheimagentnr1.mumbleintegration.config.gui.list.ConfigList;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.TooltipAccessor;
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
		addRenderableWidget( new Button(
			width / 2 - 100,
			height - 25,
			100,
			20,
			Component.translatable( "gui.done" ),
			w -> {
				configList.save();
				onClose();
			}
		) );
		addRenderableWidget( new Button(
			width / 2 + 5,
			height - 25,
			100,
			20,
			Component.translatable( "gui.cancel" ),
			w -> onClose()
		) );
	}
	
	@Override
	public void render( @Nonnull PoseStack poseStack, int mouseX, int mouseY, float partialTicks ) {
		
		renderBackground( poseStack );
		configList.render( poseStack, mouseX, mouseY, partialTicks );
		drawCenteredString( poseStack, font, title, width / 2, 5, 16777215 );
		super.render( poseStack, mouseX, mouseY, partialTicks );
		List<FormattedCharSequence> list = tooltipAt( mouseX, mouseY );
		if( list != null ) {
			renderTooltip( poseStack, list, mouseX, mouseY );
		}
	}
	
	private List<FormattedCharSequence> tooltipAt( int mouseX, int mouseY ) {
		
		Optional<ConfigEntry> optional = configList.getMouseOver( mouseX, mouseY );
		return optional.isPresent() && optional.get() instanceof TooltipAccessor
			? ( (TooltipAccessor)optional.get() ).getTooltip()
			: ImmutableList.of();
	}
	
	@Override
	public void onClose() {
		
		Objects.requireNonNull( minecraft );
		minecraft.setScreen( parent );
	}
}
