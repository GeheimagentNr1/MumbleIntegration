package de.geheimagentnr1.mumbleintegration;

import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import net.minecraftforge.fml.IExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.network.NetworkConstants;

import javax.annotation.Nonnull;


@SuppressWarnings( "UtilityClassWithPublicConstructor" )
@Mod( MumbleIntegration.MODID )
public class MumbleIntegration {
	
	
	@Nonnull
	public static final String MODID = "mumbleintegration";
	
	public MumbleIntegration() {
		
		ModLoadingContext.get().registerConfig( ModConfig.Type.CLIENT, ClientConfig.CONFIG );
		ModLoadingContext.get().registerExtensionPoint(
			IExtensionPoint.DisplayTest.class,
			() -> new IExtensionPoint.DisplayTest(
				() -> NetworkConstants.IGNORESERVERONLY,
				( remote, isServer ) -> true
			)
		);
	}
}
