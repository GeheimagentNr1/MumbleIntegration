package de.geheimagentnr1.mumbleintegration;

import de.geheimagentnr1.mumbleintegration.config.MainConfig;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;


@SuppressWarnings( { "UtilityClassWithPublicConstructor", "unused" } )
@Mod( MumbleIntegration.MODID )
public class MumbleIntegration {
	
	
	//package-private
	@Nonnull
	static final String MODID = "mumbleintegration";
	
	public MumbleIntegration() {
		
		ModLoadingContext.get().registerConfig( ModConfig.Type.CLIENT, MainConfig.CONFIG );
		ModLoadingContext.get().registerExtensionPoint(
			ExtensionPoint.DISPLAYTEST,
			() -> Pair.of(
				() -> FMLNetworkConstants.IGNORESERVERONLY,
				( remote, isServer ) -> true
			)
		);
	}
}
