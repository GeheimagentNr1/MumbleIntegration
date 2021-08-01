package de.geheimagentnr1.mumbleintegration.handlers;

import de.geheimagentnr1.mumbleintegration.MumbleIntegration;
import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import de.geheimagentnr1.mumbleintegration.config.gui.ModConfigScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.ConfigGuiHandler;

import javax.annotation.Nonnull;


@Mod.EventBusSubscriber( modid = MumbleIntegration.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
public class ModEventHandler {
	
	
	@SubscribeEvent
	public static void handleModConfigLoadingEvent( @Nonnull ModConfigEvent.Loading event ) {
		
		ClientConfig.handleConfigChange();
	}
	
	@SubscribeEvent
	public static void handleModConfigReloadingEvent( @Nonnull ModConfigEvent.Reloading event ) {
		
		ClientConfig.handleConfigChange();
	}
	
	@SubscribeEvent
	public static void handleClientSetupEvent( @Nonnull FMLClientSetupEvent event ) {
		
		ModLoadingContext.get().registerExtensionPoint(
			ConfigGuiHandler.ConfigGuiFactory.class,
			() -> new ConfigGuiHandler.ConfigGuiFactory(
				( minecraft, screen ) -> new ModConfigScreen( screen )
			)
		);
	}
}
