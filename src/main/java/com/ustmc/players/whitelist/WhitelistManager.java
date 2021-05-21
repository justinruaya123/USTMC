package com.ustmc.players.whitelist;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.plugin.Plugin;

import com.ustmc.utils.ConfigManager;
import com.ustmc.utils.Console;
import com.ustmc.utils.Utils;

public class WhitelistManager {
	// NAME, COLLEGE
	public static Map<String, String> whitelistPlayer;
	public static Map<String, String> hashed;
	public static boolean enabled;
	Plugin plugin;

	public WhitelistManager(Plugin mainPlugin) {
		this.plugin = mainPlugin;
	}

	public Plugin getPlugin() {
		return plugin;
	}

	public static void loadManager() {
		WhitelistManager.whitelistPlayer = new HashMap<String, String>();
		hashed = new HashMap<String, String>();
	}

	public static Map<String, String> getWhitelist() {
		return whitelistPlayer;
	}

	public static Map<String, String> getHashed() {
		return hashed;
	}

	public static void toggleEnabled() {
		enabled = !enabled;
	}

	public static void setEnabled(boolean state) {
		enabled = state;
	}

	public static boolean isEnabled() {
		return enabled;
	}

	public static void clearPlayer() {
		WhitelistManager.whitelistPlayer.clear();
	}

	public static void removePlayer(String name) {
		WhitelistManager.whitelistPlayer.remove(name);
	}

	public static void addPlayer(String name, String college) {
		WhitelistManager.whitelistPlayer.put(name, college);
	}

	public static boolean contains(final String name) {
		return WhitelistManager.whitelistPlayer.keySet().contains(name);
	}

	public static void downloadSheets() {
		loadManager();
		URL download = null;
		int ctr = 0;
		try {
			download = new URL(ConfigManager.getMainConfiguration().getString("whitelist.url"));
			BufferedReader in = new BufferedReader(new InputStreamReader(download.openStream()));
			String line;
			String array[] = null;
			in.readLine(); // Initial condition
			while ((line = in.readLine()) != null) {
				array = line.split(",");
				if (array.length < 3) {
					Utils.consoleWarn("Error reading array: " + line);
					continue;
				}
				if (WhitelistManager.getWhitelist().containsKey(array[1])) {
					if (array.length >= 4) {
						if (WhitelistManager.getHashed().get(array[1]) != array[3]) {
							Console.warn("Player " + array[1] + " has duplicate email! Admins, see Hashed mail: "
									+ array[3]);
						}
					} else {
						Console.warn("Player " + array[1]
								+ " has insufficient data. Most likely an offset of manual whitelists. Player added to whitelist, but hashed ID is not stored.");
					}

				} else {
					WhitelistManager.addPlayer(array[1], array[2]);
					if (array.length >= 4) {
						WhitelistManager.getHashed().put(array[1], array[3]);
					}

					ctr++;
				}
			}
			in.close();
			Utils.consoleInfo("Whitelist added " + ctr + " players!");
		} catch (MalformedURLException e2) {
			e2.printStackTrace();
		} catch (IOException e3) {
			e3.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

}
