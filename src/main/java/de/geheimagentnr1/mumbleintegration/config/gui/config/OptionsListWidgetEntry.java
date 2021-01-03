package de.geheimagentnr1.mumbleintegration.config.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.list.AbstractOptionList;

import javax.annotation.Nonnull;


public abstract class OptionsListWidgetEntry extends AbstractOptionList.Entry<OptionsListWidgetEntry> {
	
	
	@Nonnull
	protected final Minecraft client;
	
	protected OptionsListWidgetEntry() {
		
		client = Minecraft.getInstance();
	}
}
