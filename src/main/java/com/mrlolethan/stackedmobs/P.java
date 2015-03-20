package com.mrlolethan.stackedmobs;

import java.io.File;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.mrlolethan.stackedmobs.listener.EntityListener;
import com.mrlolethan.stackedmobs.task.EntityStackerTask;

public class P extends JavaPlugin {

	private static P p;

	private final File externalConfigFile = new File(this.getDataFolder(), "config.yml");
	private Config config;
	private EntityStackerTask entityStackerTask;

	public P() {
		p = this;
	}


	@Override
	public void onEnable() {
		// Initialize the configuration
		this.config = new Config(externalConfigFile);

		// Register listeners
		Bukkit.getPluginManager().registerEvents(new EntityListener(), this);

		this.entityStackerTask = new EntityStackerTask();
		this.entityStackerTask.runTaskTimer(this, this.getConfiguration().updateTickDelay, this.getConfiguration().updateTickDelay);
	}

	public Config getConfiguration() {
		return this.config;
	}

	public static P getInstance() {
		return p;
	}

	public static void log(Level level, String message) {
		p.getLogger().log(level, message);
	}

}
