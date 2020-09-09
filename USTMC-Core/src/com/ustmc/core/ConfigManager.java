package com.ustmc.core;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigManager {
	private static main mainPlugin = null;

	private static FileConfiguration playersCFG = null;
	private static File playersFile = null;

	private static FileConfiguration mainCFG = null;
	private static File mainFile = null;

	public static FileConfiguration getPlayersConfig() {
		return playersCFG;
	}

	public static FileConfiguration getMainConfiguration() {
		return mainCFG;
	}

	public static void setPlugin(main plugin) {
		mainPlugin = plugin;
		initializePlayersConfig();
		initializeMainConfig();
	}

	public static void initializePlayersConfig() {
		playersFile = new File(Bukkit.getServer().getPluginManager().getPlugin("TigerMC").getDataFolder(),
				"playerdata.yml");
		if (!playersFile.exists()) {
			try {
				playersFile.createNewFile();
				playersFile.mkdir();
				mainPlugin.getLogger().log(Level.CONFIG, "[TigerMC] Created 'playerdata.yml'!");
			} catch (IOException e) {
				mainPlugin.getLogger().log(Level.SEVERE, "[TigerMC] Could NOT create 'playerdata.yml'!");
				e.printStackTrace();
			}
		}
		playersCFG = YamlConfiguration.loadConfiguration(playersFile);
	}

	public static void initializeMainConfig() {
		mainFile = new File(Bukkit.getServer().getPluginManager().getPlugin("TigerMC").getDataFolder(), "main.yml");
		if (!mainFile.exists()) {
			try {
				mainFile.createNewFile();
				mainFile.mkdir();
				mainPlugin.getLogger().log(Level.CONFIG, "[TigerMC] Created 'main.yml'!");
			} catch (IOException e) {
				mainPlugin.getLogger().log(Level.SEVERE, "[TigerMC] Could NOT create 'main.yml'!");
				e.printStackTrace();
			}
		}
		mainCFG = YamlConfiguration.loadConfiguration(mainFile);
	}

	public static void saveMain() {
		try {
			mainCFG.save(mainFile);
		} catch (Exception e) {
			mainPlugin.getLogger().log(Level.SEVERE, "[TigerMC] Could NOT save 'main.yml'!");
		}
	}

	public static void savePlayers() {
		try {
			playersCFG.save(playersFile);
		} catch (Exception e) {
			mainPlugin.getLogger().log(Level.SEVERE, "[TigerMC] Could NOT save 'main.yml'!");
		}
	}

	public static void save() {
		saveMain();
		savePlayers();
	}

	public static void reloadPlayersFile() {
		playersCFG = YamlConfiguration.loadConfiguration(playersFile);
	}

	public static void reloadMainFile() {
		mainCFG = YamlConfiguration.loadConfiguration(mainFile);
	}

	public static void reloadAll() {
		reloadPlayersFile();
		reloadMainFile();
	}

}
