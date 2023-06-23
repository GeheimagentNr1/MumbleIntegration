package de.geheimagentnr1.mumbleintegration;

import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import de.geheimagentnr1.mumbleintegration.linking.MumbleLinker;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;


@Mod( MumbleIntegration.MODID )
public class MumbleIntegration extends AbstractMod {
	
	
	@NotNull
	static final String MODID = "mumbleintegration";
	
	@NotNull
	@Override
	public String getModId() {
		
		return MODID;
	}
	
	@Override
	protected void initMod() {
		
		DistExecutor.safeRunWhenOn(
			Dist.CLIENT,
			() -> () -> {
				MumbleLinker mumbleLinker = registerEventHandler( new MumbleLinker( this ) );
				registerConfig( abstractMod -> new ClientConfig( abstractMod, mumbleLinker ) );
			}
		);
	}
}
