package de.geheimagentnr1.mumbleintegration.config.gui.list.values;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;


public class BooleanConfigEntry extends ValueConfigEntry<Boolean> {
	
	
	@Nonnull
	private final Button button;
	
	public BooleanConfigEntry(
		@Nonnull Minecraft _minecraft,
		@Nonnull String title,
		@Nonnull String description,
		@Nonnull Boolean value,
		@Nonnull Consumer<Boolean> saver ) {
		
		super( _minecraft, title, description, value, saver );
		button = new Button( 220, 0, 100, 20, buildShownText(), pressedButton -> {
			setValue( !getValue() );
			pressedButton.setMessage( buildShownText() );
		} );
	}
	
	private Component buildShownText() {
		
		return Component.literal( getValue() ? "Yes" : "No" );
	}
	
	@Override
	protected void drawValue( @Nonnull PoseStack poseStack, int rowTop, int mouseX, int mouseY, float partialTicks ) {
		
		button.y = rowTop;
		button.render( poseStack, mouseX, mouseY, partialTicks );
	}
	
	@Nonnull
	@Override
	public List<? extends NarratableEntry> narratables() {
		
		return ImmutableList.of( button );
	}
	
	@Nonnull
	@Override
	public List<? extends GuiEventListener> children() {
		
		return ImmutableList.of( button );
	}
}
