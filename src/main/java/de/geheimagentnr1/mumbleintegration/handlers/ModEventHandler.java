package de.geheimagentnr1.mumbleintegration.handlers;

import de.geheimagentnr1.mumbleintegration.config.MainConfig;
import de.geheimagentnr1.mumbleintegration.config.gui.ModGuiConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nonnull;


@SuppressWarnings( "unused" )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
public class ModEventHandler {
	
	
	@SubscribeEvent
	public static void handleModConfigLoadingEvent( @Nonnull ModConfig.Loading event ) {
		
		MainConfig.handleConfigChange();
	}
	
	@SubscribeEvent
	public static void handleModConfigReloadingEvent( @Nonnull ModConfig.Reloading event ) {
		
		MainConfig.handleConfigChange();
	}
	
	@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	public static void handleClientSetupEvent( @Nonnull FMLClientSetupEvent event ) {
		
		ModLoadingContext.get().registerExtensionPoint( ExtensionPoint.CONFIGGUIFACTORY, () -> ( mc, parent ) ->
			new ModGuiConfig( parent ) );
	}
}
