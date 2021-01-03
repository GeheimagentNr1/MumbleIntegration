package de.geheimagentnr1.mumbleintegration.handlers;

import de.geheimagentnr1.mumbleintegration.linking.MumbleLinking;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;


@SuppressWarnings( "unused" )
@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.FORGE )
public class ForgeEventHandler {
	
	
	@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	public static void handleLoggedInEvent( @Nonnull ClientPlayerNetworkEvent.LoggedInEvent event ) {
		
		MumbleLinking.link();
	}
	
	@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	public static void handleLoggedOutEvent( @Nonnull ClientPlayerNetworkEvent.LoggedOutEvent event ) {
		
		MumbleLinking.unlink();
	}
	
	@OnlyIn( Dist.CLIENT )
	@SubscribeEvent
	public static void handleClientTickEvent( @Nonnull TickEvent.ClientTickEvent event ) {
		
		MumbleLinking.updateData();
	}
}
