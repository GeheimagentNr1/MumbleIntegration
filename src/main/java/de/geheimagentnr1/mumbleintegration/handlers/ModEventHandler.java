package de.geheimagentnr1.mumbleintegration.handlers;

import de.geheimagentnr1.mumbleintegration.MumbleIntegration;
import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import de.geheimagentnr1.mumbleintegration.config.gui.ModGuiConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nonnull;


@Mod.EventBusSubscriber( modid = MumbleIntegration.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT )
public class ModEventHandler {
	
	
	@SubscribeEvent
	public static void handleModConfigLoadingEvent( @Nonnull ModConfig.Loading event ) {
		
		ClientConfig.handleConfigChange();
	}
	
	@SubscribeEvent
	public static void handleModConfigReloadingEvent( @Nonnull ModConfig.ConfigReloading event ) {
		
		ClientConfig.handleConfigChange();
	}
	
	@SubscribeEvent
	public static void handleClientSetupEvent( @Nonnull FMLClientSetupEvent event ) {
		
		ModLoadingContext.get().registerExtensionPoint(
			ExtensionPoint.CONFIGGUIFACTORY,
			() -> ( mc, parent ) -> new ModGuiConfig( parent )
		);
	}
}
