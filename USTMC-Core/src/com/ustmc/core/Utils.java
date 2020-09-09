package com.ustmc.core;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Utils {
	public static Vector vectorFrom(Location from, Location to) {
		double vecX = to.getX() - from.getX();
		double vecY = to.getY() - from.getY();
		double vecZ = to.getZ() - from.getZ();
		return new Vector(vecX, vecY, vecZ).normalize();
	}

	public static Vector vectorTo(Location from, Location to) {
		return vectorFrom(from, to).multiply(-1.0f);
	}
	
	public static String announceToChat(String message) {
		return ChatColor.GREEN + "[" + ChatColor.YELLOW + "TIGERMC" + ChatColor.GREEN + "] " + ChatColor.ITALIC
				+ ChatColor.LIGHT_PURPLE + message;
	}
}
