package de.geheimagentnr1.mumbleintegration.config.gui.list.values;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.TextComponent;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;


public abstract class InputConfigEntry<T> extends ValueConfigEntry<T> {
	
	@Nonnull
	private final EditBox textField;
	
	//package-private
	InputConfigEntry(
		@Nonnull Minecraft _minecraft,
		@Nonnull String title,
		@Nonnull String description,
		@Nonnull T value,
		@Nonnull Consumer<T> saver,
		@Nonnull Predicate<String> filter ) {
		
		super( _minecraft, title, description, value, saver );
		textField = new EditBox( minecraft.font, 220, 0, 98, 18, new TextComponent( "" ) );
		textField.setResponder( this::setInputValue );
		textField.setValue( String.valueOf( getValue() ) );
		textField.setFilter( filter );
	}
	
	@Override
	protected void drawValue(
		@Nonnull PoseStack poseStack,
		int rowTop,
		int mouseX,
		int mouseY,
		float partialTicks ) {
		
		textField.y = rowTop;
		textField.render( poseStack, mouseX, mouseY, partialTicks );
	}
	
	//package-private
	abstract void setInputValue( @Nonnull String text );
	
	@Nonnull
	@Override
	public List<? extends NarratableEntry> narratables() {
		
		return ImmutableList.of( textField );
	}
	
	@Nonnull
	@Override
	public List<? extends GuiEventListener> children() {
		
		return ImmutableList.of( textField );
	}
}
