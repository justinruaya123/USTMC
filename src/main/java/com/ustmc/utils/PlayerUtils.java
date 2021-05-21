package com.ustmc.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class PlayerUtils {

	public static List<Player> getNearbyPlayers(Player player, double radius) {
		return getNearbyPlayers(player.getLocation(), radius);
	}

	public static List<Player> getNearbyPlayers(Location loc, double radius) {
		List<Player> players = new ArrayList<Player>();
		double d2 = radius * radius;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getLocation().distanceSquared(loc) <= d2) {
				players.add(player);
			}
		}
		return players;
	}

	public static Player getNearestPlayer(Player player) {
		return getNearestPlayer(player.getLocation());
	}

	public static Player getNearestPlayer(Location loc) {
		Player nearest = null;
		for (Player player : Bukkit.getOnlinePlayers()) {
			if (player.getLocation().equals(loc))
				continue;
			if (nearest == null) {
				nearest = player;
				continue;
			}
			if (player.getLocation().distanceSquared(loc) < nearest.getLocation().distanceSquared(loc)) {
				nearest = player;
			}
		}
		return nearest;
	}
}
