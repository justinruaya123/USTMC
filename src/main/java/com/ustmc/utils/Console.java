package com.ustmc.utils;

import java.util.logging.Logger;

import org.bukkit.ChatColor;

public class Console {
	private static Logger log;
	public static String startTigerMC = ChatColor.GREEN + "[" + ChatColor.YELLOW + "TigerMC" + ChatColor.RED + "] ";

	public static void enable() {
		log = Logger.getLogger("Minecraft");
	}

	public static void warn(String message) {
		log.warning(startTigerMC + ChatColor.RED + message);
	}

	public static void severe(String message) {
		log.severe(startTigerMC + ChatColor.DARK_RED + message);
	}

	public static void info(String message) {
		log.info(startTigerMC + ChatColor.LIGHT_PURPLE + message);
	}

	public static Logger getLogger() {
		return log;
	}
}
