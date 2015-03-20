package com.mrlolethan.stackedmobs;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import com.mrlolethan.stackedmobs.util.FileUtils;

public class Config {

	private YamlConfiguration config = new YamlConfiguration();
	private File file;

	/** The mob stack radius. Mobs of the same type within this radius will be stacked. */
	public int stackRadius = 1;
	/** The mob types that we want to stack. Ignore {@code EntityType}s not in this {@code Set}. */
	public Set<EntityType> mobTypes = new HashSet<EntityType>();
	/** The update delay for mob stacking, in ticks. */
	public int updateTickDelay = 20;
	/**
	 * The color of stacked mobs' name (displays number of mobs in the stack).
	 *  Changing the mob name's color prevents duping with nametags.
	 */
	public ChatColor stackNumberColor = ChatColor.WHITE;


	public Config(File file) {
		this.file = file;

		this.initDefaults();
		this.loadConfig();
	}


	public void initDefaults() {
		if (!file.exists()) {
			try {
				file.getParentFile().mkdirs();
				FileUtils.copyInputStreamToFile(P.class.getResourceAsStream("/" + file.getName()), file);
			} catch (IOException ex) {
				P.log(Level.WARNING, "IOException thrown whilst saving default config file: " + ex.getMessage());
			}
		}
	}

	public void loadConfig() {
		try {
			config.load(file);
		} catch (IOException | InvalidConfigurationException ex) {
			P.log(Level.SEVERE, "An exception was thrown whilst loading config: " + ex.getMessage());
			return;
		}

		this.stackRadius = this.config.getInt("StackRadius", this.stackRadius);
		this.compileEntityTypesList(this.config.getStringList("MobTypes")); // Load EntityTypes list (mobTypes)
		this.updateTickDelay = this.config.getInt("UpdateTickDelay");
		this.stackNumberColor = this.getChatColor(this.config.getString("StackNumberColor"), this.stackNumberColor);
	}

	/*
	 * Helper methods
	 */
	private void compileEntityTypesList(List<String> list) {
		if (list == null) return; // Don't iterate over a null list!

		for (String entityName : list) {
			try {
				EntityType entityType = EntityType.valueOf(entityName.toUpperCase());
				this.mobTypes.add(entityType);
			} catch (IllegalArgumentException ex) {
				P.log(Level.WARNING, "Invalid mob type: " + entityName);
			}
		}
	}

	private ChatColor getChatColor(String code, ChatColor defaultValue) {
		ChatColor color = ChatColor.getByChar(code);
		if (color == null) {
			P.log(Level.WARNING, "Invalid formatting code: '" + code + "'");
			return defaultValue;
		}
		return color;
	}

}
