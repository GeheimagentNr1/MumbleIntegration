package de.geheimagentnr1.mumbleintegration;

import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import de.geheimagentnr1.mumbleintegration.linking.MumbleLinker;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.jetbrains.annotations.NotNull;


@Mod( value = MumbleIntegration.MODID, dist = Dist.CLIENT )
public class MumbleIntegration {
	
	
	@NotNull
	public static final String MODID = "mumbleintegration";
	
	@NotNull
	private final ClientConfig clientConfig;
	
	@NotNull
	private final MumbleLinker mumbleLinker;
	
	public MumbleIntegration( @NotNull IEventBus modEventBus, @NotNull ModContainer modContainer ) {
		
		mumbleLinker = new MumbleLinker();
		clientConfig = new ClientConfig( mumbleLinker );
		
		modContainer.registerConfig( ModConfig.Type.CLIENT, clientConfig.getSpec() );
		
		NeoForge.EVENT_BUS.register( mumbleLinker );
		
		modEventBus.addListener( this::onClientSetup );
	}
	
	private void onClientSetup( @NotNull FMLClientSetupEvent event ) {
		
		mumbleLinker.setClientConfig( clientConfig );
		clientConfig.registerConfigScreen();
	}
	
	@NotNull
	public ClientConfig getClientConfig() {
		
		return clientConfig;
	}
}
