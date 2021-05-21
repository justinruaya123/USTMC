package com.ustmc.players;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.ustmc.players.whitelist.WhitelistManager;
import com.ustmc.utils.ConfigManager;
import com.ustmc.utils.Utils;

public class TigerPlayer {

	private static Map<UUID, TigerPlayer> players;
	private static Map<String, UUID> loadedPlayers;

	private UUID uuid;
	private HashSet<UUID> crushes;
	private ConfigurationSection config;
	private String configIndex;
	private College college;

	private HashMap<String, Boolean> booleanList;
	private HashMap<String, Double> doubleList;
	private HashMap<String, Integer> integerList;
	private HashMap<String, String> stringList;

	public TigerPlayer(Player player) {
		this(Bukkit.getOfflinePlayer(player.getUniqueId()));
	}

	public TigerPlayer(OfflinePlayer player) {
		this.uuid = player.getUniqueId();
		crushes = new HashSet<UUID>();
		booleanList = new HashMap<String, Boolean>();
		doubleList = new HashMap<String, Double>();
		integerList = new HashMap<String, Integer>();
		stringList = new HashMap<String, String>();
		configIndex = "data.players." + uuid.toString();
		if (ConfigManager.getPlayersConfig()
				.isConfigurationSection("data.players." + player.getUniqueId().toString())) {
			config = ConfigManager.getPlayersConfig().getConfigurationSection(configIndex);
		} else {
			config = ConfigManager.getPlayersConfig().createSection(configIndex);
			for (DefaultPlayerBoolean value : DefaultPlayerBoolean.values()) {
				setBool(value.getConfigIndex(), value.getValue(), false);
			}
			for (DefaultPlayerInteger value : DefaultPlayerInteger.values()) {
				setInt(value.getConfigIndex(), value.getValue(), false);
			}
			setString("Name", player.getName(), false);
		}
		loadToMemory();
		if (!stringList.containsKey("College")) {
			setString("College", WhitelistManager.getWhitelist().get(player.getName()), false);
		}
		college = College.getCollegeComplete(stringList.get("College"));
		TigerPlayer.getPlayers().put(this.uuid, this);
		saveToPlayersConfig();
	}

	public boolean addCrush(UUID crush) {
		if (crushes.contains(crush)) {
			return false;
		}
		crushes.add(crush);
		saveCrushes(true);
		return true;
	}

	public boolean removeCrush(UUID crush) {
		if (crushes.contains(crush)) {
			return false;
		}
		crushes.remove(crush);
		saveCrushes(true);
		return true;
	}

	public boolean isCrush(UUID candidate) {
		return crushes.contains(candidate);
	}

	public boolean getBool(String index) {
		String key = "data.players." + uuid.toString() + ".bool." + index;
		if (booleanList.containsKey(index)) {
			return booleanList.get(index);
		}
		if (ConfigManager.getPlayersConfig().isBoolean(key)) {
			boolean value = ConfigManager.getPlayersConfig().getBoolean(key);
			booleanList.put(index, value);
			return value;
		}
		Bukkit.getServer().getLogger().warning(ChatColor.YELLOW + "TigerMC: " + ChatColor.RED
				+ "Error loading boolean with index " + ChatColor.DARK_RED + key);
		return false;
	}

	public double getDouble(String index) {
		String key = "data.players." + uuid.toString() + ".double." + index;
		if (doubleList.containsKey(index)) {
			return doubleList.get(index);
		}
		if (ConfigManager.getPlayersConfig().isDouble(key)) {
			double value = ConfigManager.getPlayersConfig().getDouble(key);
			doubleList.put(index, value);
			return value;
		}
		Bukkit.getServer().getLogger().warning(ChatColor.YELLOW + "TigerMC: " + ChatColor.RED
				+ "Error loading double with index " + ChatColor.DARK_RED + key);
		return -Integer.MAX_VALUE;
	}

	public int getInt(String index) {
		String key = "data.players." + uuid.toString() + ".int." + index;
		if (integerList.containsKey(index)) {
			return integerList.get(index);
		}
		if (ConfigManager.getPlayersConfig().isInt(key)) {
			int value = ConfigManager.getPlayersConfig().getInt(key);
			integerList.put(index, value);
			return value;
		}
		Bukkit.getServer().getLogger().warning(ChatColor.YELLOW + "TigerMC: " + ChatColor.RED
				+ "Error loading integer with index " + ChatColor.DARK_RED + key);
		return -Integer.MAX_VALUE;
	}

	public String getString(String index) {
		String key = "data.players." + uuid.toString() + ".int." + index;
		if (stringList.containsKey(index)) {
			return stringList.get(index);
		}
		if (ConfigManager.getPlayersConfig().isString(key)) {
			String value = ConfigManager.getPlayersConfig()
					.getString("data.players." + uuid.toString() + ".string." + index);
			stringList.put(index, value);
			return value;
		}
		return ChatColor.DARK_RED + "null";
	}

	public boolean setBool(String index, boolean value, boolean save) {
		if (!booleanList.containsKey(index) || booleanList.get(index) != value) {
			booleanList.put(index, value);
			if (save) {
				ConfigManager.getPlayersConfig().set("data.players." + uuid.toString() + ".bool." + index, value);
			}
			return true;
		}
		return false;
	}

	public boolean setDouble(String index, double value, boolean save) {
		if (!doubleList.containsKey(index) || doubleList.get(index) != value) {
			doubleList.put(index, value);
			if (save) {
				ConfigManager.getPlayersConfig().set("data.players." + uuid.toString() + ".double." + index, value);
			}
			return true;
		}
		return false;
	}

	public boolean setInt(String index, int value, boolean save) {
		if (!integerList.containsKey(index) || integerList.get(index) != value) {
			integerList.put(index, value);
			if (save) {
				ConfigManager.getPlayersConfig().set("data.players." + uuid.toString() + ".int." + index, value);
			}
			return true;
		}
		return false;
	}

	public boolean setString(String index, String value, boolean save) {
		if (!stringList.containsKey(index) || stringList.get(index) != value) {
			stringList.put(index, value);
			if (save) {
				ConfigManager.getPlayersConfig().set("data.players." + uuid.toString() + ".string." + index, value);
			}
			return true;
		}
		return false;
	}

	public void saveBool() {
		if (booleanList.keySet().size() == 0)
			return;
		for (String key : booleanList.keySet()) {
			ConfigManager.getPlayersConfig().set("data.players." + uuid.toString() + ".bool." + key,
					booleanList.get(key));
		}
	}

	public void saveDouble() {
		if (booleanList.keySet().size() == 0)
			return;
		for (String key : doubleList.keySet()) {
			ConfigManager.getPlayersConfig().set("data.players." + uuid.toString() + ".double." + key,
					doubleList.get(key));
		}
	}

	public void saveInt() {
		if (integerList.keySet().size() == 0)
			return;
		for (String key : integerList.keySet()) {
			ConfigManager.getPlayersConfig().set("data.players." + uuid.toString() + ".int." + key,
					integerList.get(key));
		}
	}

	public void saveString() {
		if (stringList.keySet().size() == 0)
			return;
		for (String key : stringList.keySet()) {
			ConfigManager.getPlayersConfig().set("data.players." + uuid.toString() + ".string." + key,
					stringList.get(key));
		}
	}

	public void saveCrushes(boolean write) {
		ConfigManager.getPlayersConfig().set("data.players." + uuid.toString() + ".crushes",
				uuidToStringArray(crushes));
		if (write)
			ConfigManager.savePlayers();
	}

	public boolean loadDataInt() {
		for (String str : config.getConfigurationSection("int").getKeys(true)) {
			if (!config.isInt("int." + str)) {
				continue;
			}
			integerList.put(str, config.getInt("int." + str));
		}
		return true;
	}

	public boolean loadDataString() {
		for (String str : config.getConfigurationSection("string").getKeys(true)) {
			if (!config.isString("string." + str)) {
				continue;
			}
			stringList.put(str, config.getString("string." + str));
		}
		return true;
	}

	public boolean loadDataDouble() {
		for (String str : config.getConfigurationSection("double").getKeys(true)) {
			if (!config.isDouble("double." + str)) {
				continue;
			}
			doubleList.put(str, config.getDouble("double." + str));
		}
		return true;
	}

	public boolean loadDataBoolean() {
		for (String str : config.getConfigurationSection("bool").getKeys(true)) {
			if (!config.isBoolean("bool." + str)) {
				continue;
			}
			booleanList.put(str, config.getBoolean("bool." + str));
		}
		return true;
	}

	public Map<String, Boolean> getBooleanList() {
		return booleanList;
	}

	public Map<String, Double> getDoubleList() {
		return doubleList;
	}

	public Map<String, Integer> getIntList() {
		return integerList;
	}

	public Map<String, String> getStringList() {
		return stringList;
	}

	public void saveToPlayersConfig() {
		saveBool();
		saveDouble();
		saveString();
		saveInt();
		saveCrushes(true);
	}

	private List<String> uuidToStringArray(HashSet<UUID> array) {
		List<String> elements = new ArrayList<String>();
		for (UUID element : array) {
			elements.add(element.toString());
		}
		return elements;
	}

	public boolean isOnline() {
		return Bukkit.getServer().getPlayer(uuid) == null;
	}

	public Player getPlayer() {
		return Bukkit.getServer().getPlayer(uuid);
	}

	public College getCollege() {
		return college;
	}

	public ConfigurationSection getConfigPath() {
		return config;
	}

	public OfflinePlayer getOfflinePlayer() {
		return Bukkit.getServer().getOfflinePlayer(uuid);
	}

	public UUID getUUID() {
		return uuid;
	}

	public boolean loadToMemory() {
		try {
			loadDataBoolean();
			loadDataDouble();
			loadDataInt();
			loadDataString();
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// STATIC METHODS

	public static Map<UUID, TigerPlayer> getPlayers() {
		return players;
	}

	public static void initializePlayers() {
		players = new HashMap<UUID, TigerPlayer>();
		loadedPlayers = new HashMap<String, UUID>();
		if (ConfigManager.getPlayersConfig().isConfigurationSection("data.players")) {
			if (ConfigManager.getPlayersConfig().getConfigurationSection("data.players").getKeys(false).size() != 0) {
				for (String str : ConfigManager.getPlayersConfig().getConfigurationSection("data.players")
						.getKeys(false)) {
					loadedPlayers.put(
							ConfigManager.getPlayersConfig().getString("data.players." + str + ".string.Name"),
							UUID.fromString(str));
				}
			}
		} else {
			ConfigManager.getPlayersConfig().createSection("data.players");
			ConfigManager.savePlayers();
		}
		Bukkit.getServer().getLogger().info(ChatColor.GREEN + "Loaded " + loadedPlayers.size() + " data!");
	}

	public static Set<String> getAllPlayerNames() {
		return loadedPlayers.keySet();
	}

	public static UUID getUUIDFromNameSearch(String preciseName) {
		for (String str : getAllPlayerNames()) {
			if (str.toLowerCase().startsWith(preciseName.toLowerCase())) {
				return loadedPlayers.get(str);
			}
		}
		return Utils.INVALID_UUID;
	}

	public static UUID getUUIDFromNameExact(String preciseName) {
		if (loadedPlayers.containsKey(preciseName)) {
			return loadedPlayers.get(preciseName);
		}
		return Utils.INVALID_UUID;
	}

	public static Map<String, UUID> getIndexedPlayers() {
		return loadedPlayers;
	}

	public static boolean isPlayer(String candidate) {
		return loadedPlayers.containsKey(candidate);
	}

}
