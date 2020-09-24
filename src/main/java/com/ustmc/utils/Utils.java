package com.ustmc.utils;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Utils {
	public static final UUID INVALID_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

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

	public static boolean isValid(double number) {
		return number != -Integer.MAX_VALUE;
	}

	public static boolean isValid(int number) {
		return number != -Integer.MAX_VALUE;
	}

	public static boolean isValid(String string) {
		return string != ChatColor.DARK_RED + "null";
	}

	public static boolean isValid(UUID uuid) {
		return !(uuid == INVALID_UUID);
	}

}
