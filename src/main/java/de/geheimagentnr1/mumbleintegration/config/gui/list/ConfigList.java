package de.geheimagentnr1.mumbleintegration.config.gui.list;

import de.geheimagentnr1.mumbleintegration.config.ClientConfig;
import de.geheimagentnr1.mumbleintegration.config.gui.list.values.BooleanConfigEntry;
import de.geheimagentnr1.mumbleintegration.config.gui.list.values.IntegerConfigEntry;
import de.geheimagentnr1.mumbleintegration.config.gui.list.values.StringConfigEntry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;

import java.util.List;
import java.util.Optional;


public class ConfigList extends ContainerObjectSelectionList<ConfigEntry> {
	
	
	public ConfigList( Minecraft _minecraft, int _width, int _height, int _y0, int _y1, int _itemHeight ) {
		
		super( _minecraft, _width, _height, _y0, _y1, _itemHeight );
		init();
	}
	
	private void init() {
		
		replaceEntries( List.of(
			new BooleanConfigEntry(
				minecraft,
				"Mumble Active",
				"Should the mumble integration be activ?",
				ClientConfig.isMumbleActive(),
				ClientConfig::setMumbleActive
			),
			new BooleanConfigEntry( minecraft,
				"Auto Connect",
				"Should mumble be started automated?",
				ClientConfig.shouldAutoConnect(),
				ClientConfig::setAutoConnect
			),
			new StringConfigEntry( minecraft,
				"Address",
				"Address of the mumble server.",
				ClientConfig.getAddress(),
				ClientConfig::setAddress
			),
			new IntegerConfigEntry( minecraft,
				"Port",
				"Port of the mumble server.",
				ClientConfig.getPort(),
				ClientConfig::setPort
			),
			new StringConfigEntry( minecraft,
				"Path",
				"Path of the mumble channel.",
				ClientConfig.getPath(),
				ClientConfig::setPath
			),
			new BooleanConfigEntry( minecraft,
				"Use Dimension Channels",
				"Use subchannels for each dimension?",
				ClientConfig.useDimensionChannels(),
				ClientConfig::setUseDimensionChannels
			)
		) );
	}
	
	public Optional<ConfigEntry> getMouseOver( double mouseX, double mouseY ) {
		
		for( ConfigEntry entry : children() ) {
			if( entry.isMouseOver( mouseX, mouseY ) ) {
				return Optional.of( entry );
			}
		}
		return Optional.empty();
	}
	
	public void save() {
		
		children().forEach( ConfigEntry::save );
	}
	
	@Override
	protected int getScrollbarPosition() {
		
		return super.getScrollbarPosition() + 32;
	}
	
	@Override
	public int getRowWidth() {
		
		return 400;
	}
}
