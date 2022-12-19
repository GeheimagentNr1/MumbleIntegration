package de.geheimagentnr1.mumbleintegration.config.gui.list;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;


public abstract class ConfigEntry extends ContainerObjectSelectionList.Entry<ConfigEntry> {
	
	
	@Nonnull
	protected final Minecraft minecraft;
	
	@Nonnull
	private final String title;
	
	@Nonnull
	private final Component description;
	
	
	protected ConfigEntry( @Nonnull Minecraft _minecraft, @Nonnull String _title, @Nonnull String _description ) {
		
		minecraft = _minecraft;
		title = _title;
		description = Component.literal( _description );
	}
	
	@Override
	public void render(
		@Nonnull PoseStack poseStack,
		int index,
		int rowTop,
		int rowLeft,
		int width,
		int height,
		int mouseX,
		int mouseY,
		boolean hovered,
		float partialTicks ) {
		
		minecraft.font.draw(
			poseStack,
			title,
			rowLeft + 80,
			rowTop + (float)( height / 4 + minecraft.font.lineHeight / 2 ),
			Objects.requireNonNull( ChatFormatting.WHITE.getColor() )
		);
		drawValue( poseStack, rowTop, mouseX, mouseY, partialTicks );
	}
	
	protected abstract void drawValue(
		@Nonnull PoseStack poseStack,
		int rowTop,
		int mouseX,
		int mouseY,
		float partialTicks );
	
	public abstract void save();
	
	@Nonnull
	public List<FormattedCharSequence> getTooltip() {
		
		return minecraft.font.split( description, 200 );
	}
}
