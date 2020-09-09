package com.ustmc.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.ustmc.core.ConfigManager;

public class TigerPlayer {

	public static Map<UUID, TigerPlayer> players;

	private Player p;
	private HashSet<UUID> crushes;

	private HashMap<String, Boolean> booleanList;
	private HashMap<String, Double> doubleList;

	public TigerPlayer(Player player) {
		this.p = player;
		crushes = new HashSet<UUID>();
		booleanList = new HashMap<String, Boolean>();
		doubleList = new HashMap<String, Double>();
	}

	public static void loadPlayers() {
		players = new HashMap<UUID, TigerPlayer>();
	}

	public boolean addCrush(UUID crush) {
		if (crushes.contains(crush)) {
			return false;
		}
		crushes.add(crush);
		saveCrushes();
		return true;
	}

	public boolean removeCrush(UUID crush) {
		if (crushes.contains(crush)) {
			return false;
		}
		crushes.remove(crush);
		saveCrushes();
		return true;
	}

	public boolean isCrush(UUID candidate) {
		return crushes.contains(candidate);
	}

	public boolean getBool(String index) {
		return booleanList.get(index);
	}

	public double getDouble(String index) {
		return doubleList.get(index);
	}

	public boolean setBool(String index, boolean value) {
		if (!booleanList.containsKey(index) || booleanList.get(index) != value) {
			booleanList.put(index, value);
			return true;
		}
		return false;
	}

	public boolean setDouble(String index, double value) {
		if (!doubleList.containsKey(index) || doubleList.get(index) != value) {
			doubleList.put(index, value);
			return true;
		}
		return false;
	}

	public void saveBool() {
		if (booleanList.keySet().size() == 0)
			return;
		for (String key : booleanList.keySet()) {
			ConfigManager.getPlayersConfig().set("data.players." + p.getUniqueId().toString() + "." + key,
					booleanList.get(key));
		}
	}

	public void saveDouble() {
		if (booleanList.keySet().size() == 0)
			return;
		for (String key : doubleList.keySet()) {
			ConfigManager.getPlayersConfig().set("data.players." + p.getUniqueId().toString() + "." + key,
					doubleList.get(key));
		}
	}

	public void saveCrushes() {
		ConfigManager.getPlayersConfig().set("data.players." + p.getUniqueId().toString() + ".crushes",
				uuidToStringArray(crushes));
		ConfigManager.savePlayers();
	}

	public void saveToPlayersConfig() {
		saveBool();
		saveDouble();
		saveCrushes();
	}

	private List<String> uuidToStringArray(HashSet<UUID> array) {
		List<String> elements = new ArrayList<String>();
		for (UUID element : array) {
			elements.add(element.toString());
		}
		return elements;
	}
	// STATIC METHODS

	public static TigerPlayer getPlayer(UUID player) {

		return null;
	}

}
