package de.geheimagentnr1.mumbleintegration.config.gui;

import de.geheimagentnr1.minecraft_forge_api.AbstractMod;
import de.geheimagentnr1.minecraft_forge_api.config.gui.AbstractConfigScreen;
import de.geheimagentnr1.minecraft_forge_api.config.gui.list.ConfigEntry;
import de.geheimagentnr1.minecraft_forge_api.config.gui.list.values.BooleanConfigEntry;
import de.geheimagentnr1.minecraft_forge_api.config.gui.list.values.IntegerConfigEntry;
import de.geheimagentnr1.minecraft_forge_api.config.gui.list.values.StringConfigEntry;
import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;


public class ModConfigScreen extends AbstractConfigScreen<ClientConfig> {
	
	
	public ModConfigScreen(
		@NotNull AbstractMod _abstractMod,
		@NotNull ClientConfig _config,
		@NotNull Screen _parent ) {
		
		super( _abstractMod, _config, _parent );
	}
	
	@NotNull
	@Override
	protected List<ConfigEntry> configEntries() {
		
		Objects.requireNonNull( minecraft );
		return List.of(
			new BooleanConfigEntry(
				minecraft,
				"Mumble Active",
				"Should the mumble integration be activ?",
				config.isMumbleActive(),
				config::setMumbleActive
			),
			new BooleanConfigEntry(
				minecraft,
				"Auto Connect",
				"Should mumble be started automated?",
				config.shouldAutoConnect(),
				config::setAutoConnect
			),
			new StringConfigEntry(
				minecraft,
				"Address",
				"Address of the mumble server.",
				config.getAddress(),
				config::setAddress
			),
			new IntegerConfigEntry(
				minecraft,
				"Port",
				"Port of the mumble server.",
				config.getPort(),
				config::setPort
			),
			new StringConfigEntry(
				minecraft,
				"Path",
				"Path of the mumble channel.",
				config.getPath(),
				config::setPath
			),
			new BooleanConfigEntry(
				minecraft,
				"Use Dimension Channels",
				"Use subchannels for each dimension?",
				config.useDimensionChannels(),
				config::setUseDimensionChannels
			)
		);
	}
}
