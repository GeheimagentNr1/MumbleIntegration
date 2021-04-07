package de.geheimagentnr1.mumbleintegration.handlers;

import de.geheimagentnr1.mumbleintegration.MumbleIntegration;
import de.geheimagentnr1.mumbleintegration.linking.MumbleLinker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;


@Mod.EventBusSubscriber( modid = MumbleIntegration.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT )
public class ForgeEventHandler {
	
	
	@Nonnull
	private static final Logger LOGGER = LogManager.getLogger( ForgeEventHandler.class );
	
	@SubscribeEvent
	public static void handleLoggedInEvent( @Nonnull ClientPlayerNetworkEvent.LoggedInEvent event ) {
		
		MumbleLinker.link();
	}
	
	@SubscribeEvent
	public static void handleLoggedOutEvent( @Nonnull ClientPlayerNetworkEvent.LoggedOutEvent event ) {
		
		MumbleLinker.unlink();
	}
	
	@SubscribeEvent
	public static void handleClientTickEvent( @Nonnull TickEvent.ClientTickEvent event ) {
		
		try {
			MumbleLinker.updateData();
		} catch( @SuppressWarnings( "ProhibitedExceptionCaught" ) NullPointerException exception ) {
			LOGGER.error( "Error during mumble data update", exception );
		}
	}
}
