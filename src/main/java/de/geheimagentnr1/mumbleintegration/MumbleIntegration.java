package de.geheimagentnr1.mumbleintegration;

import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

import javax.annotation.Nonnull;


@SuppressWarnings( "UtilityClassWithPublicConstructor" )
@Mod( MumbleIntegration.MODID )
public class MumbleIntegration {
	
	
	@Nonnull
	public static final String MODID = "mumbleintegration";
	
	public MumbleIntegration() {
		
		ModLoadingContext.get().registerConfig( ModConfig.Type.CLIENT, ClientConfig.CONFIG );
	}
}
