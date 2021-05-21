package com.ustmc.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.util.Vector;

import com.ustmc.core.main;

public class Utils {
	public static final UUID INVALID_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	public static String startTigerMC = ChatColor.GREEN + "[" + ChatColor.YELLOW + "TigerMC" + ChatColor.GREEN + "] ";
	private static MessageDigest digest;
	public static NamespacedKey unifKey;

	public static void initialize() {
		unifKey = new NamespacedKey(main.mainInstance, "uniform-url");
		try {
			digest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	public static String hashSHA256(String message) {
		byte[] hash = digest.digest(message.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(hash);
	}

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
		return startTigerMC + ChatColor.LIGHT_PURPLE + message;
	}

	public static String messageNoPermissions() {
		return startTigerMC + ChatColor.RED + "You do not have permissions for this command!";
	}

	public static void consoleWarn(String message) {
		Bukkit.getServer().getLogger().warning(startTigerMC + ChatColor.RED + message);
	}

	public static void consoleInfo(String message) {
		Bukkit.getServer().getLogger().info(startTigerMC + ChatColor.LIGHT_PURPLE + message);
	}

	public static String messageUsage(String usage) {
		String[] args = usage.split("`");
		String argsReturn = ChatColor.YELLOW + "";
		for (String str : args) {
			if (str == args[0]) {
				argsReturn += args[0] + " ";
			} else {
				if (str.startsWith("<")) {
					argsReturn += ChatColor.GOLD + str;
				} else if (str.startsWith("(")) {
					argsReturn += ChatColor.GRAY + str;
				} else {
					argsReturn += ChatColor.BLUE + str;
				}

			}
		}
		return ChatColor.GREEN + "[" + ChatColor.YELLOW + "TigerMC" + ChatColor.GREEN + "] " + ChatColor.ITALIC
				+ ChatColor.GREEN + "Usage: " + argsReturn;
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

	public static String extractMultipleArgs(String[] args, int startIndex) {
		String finalString = "";
		for (int i = startIndex; i < args.length; i++) {
			finalString = finalString + " " + args[i];
		}
		return finalString;
	}

	public static List<String> tabCompleterSearch(Collection<String> search, String startsWith) {
		List<String> output = new ArrayList<String>();
		for (String index : search) {
			Bukkit.getServer().getLogger().info("Found " + index + " with " + startsWith);
			if (index.toLowerCase().startsWith(startsWith.toLowerCase())) {
				output.add(index);
			}
		}
		return output;
	}

}
