package com.ustmc.core;

import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.ustmc.core.CoreCommandListener;
import com.ustmc.navigation.LocatorCommand;
import com.ustmc.players.College;
import com.ustmc.players.CommandTigerplayer;
import com.ustmc.players.TigerListener;
import com.ustmc.players.TigerPlayer;
import com.ustmc.players.Uniform;
import com.ustmc.players.whitelist.WhitelistManager;
import com.ustmc.players.whitelist.WhitelistScheduler;
import com.ustmc.social.SocialCommands;
import com.ustmc.social.SocialListener;
import com.ustmc.tabs.CommandTabCrush;
import com.ustmc.utils.ConfigManager;
import com.ustmc.utils.Console;
import com.ustmc.utils.Utils;

import net.milkbowl.vault.economy.Economy;

public class main extends JavaPlugin {

	// PLUGINS
	SocialCommands socialCommands;
	LocatorCommand locatorCommand;
	SocialListener socialListener;
	CommandTigerplayer commandTigerplayer;
	Uniform uniformCommand;
	TigerListener tigerListener;
	CoreCommandListener commandListener;
	//
	public static String version = "1.22";
	public static main mainInstance;
	// VAULT INTEGRATION
	private static final Logger log = Logger.getLogger("Minecraft");
	private static Economy econ = null;

	// PLUGINS
	private static WorldGuardPlugin wg;
//	private static SkinsRestorer skinsRestorer;
//	private static SkinsRestorerBukkitAPI skinsRestorerBukkitAPI;

	// =========================
	@Override
	public void onEnable() {
		main.mainInstance = this;
		Console.enable();
		Utils.initialize();
		College.initialize();
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		ConfigManager.setPlugin(this);
		try {
			loadPlugins();
		} catch (Exception e) {
			Console.severe("There was a problem in loading dependencies!");
			e.printStackTrace();
		}
		loadWhitelist();
		if (ConfigManager.getPlayersConfig().isConfigurationSection("data.players")) {
			TigerPlayer.initializePlayers();
		} else {
			ConfigManager.getPlayersConfig().createSection("data.players");
		}
		commandTigerplayer = new CommandTigerplayer(this);
		socialCommands = new SocialCommands(this);
		locatorCommand = new LocatorCommand(this);
		socialListener = new SocialListener(this);
		uniformCommand = new Uniform(this);
		tigerListener = new TigerListener(this);
		commandListener = new CoreCommandListener(this);
		// LISTENERS
		Bukkit.getServer().getPluginManager().registerEvents(tigerListener, this);
		Bukkit.getServer().getPluginManager().registerEvents(socialListener, this);
		Bukkit.getServer().getPluginManager().registerEvents(uniformCommand, this);
		//
		getCommand("hug").setExecutor(socialCommands);
		getCommand("poke").setExecutor(socialCommands);
		getCommand("fuck").setExecutor(socialCommands);
		getCommand("crush").setExecutor(socialCommands);
		getCommand("sanaol").setExecutor(socialCommands);
		// getCommand("uncrush").setExecutor(socialCommands);
		getCommand("trackplayer").setExecutor(locatorCommand);
		getCommand("test").setExecutor(locatorCommand);

		// MAIN OR CORE UST FUNCTIONALITY
		getCommand("bell").setExecutor(commandListener);
		//
//		BuildingGames bg = new BuildingGames(this);
//		getCommand("createblock").setExecutor(bg);
//		getCommand("generateplots").setExecutor(bg);
//		getCommand("joinbg").setExecutor(bg);
//		getCommand("bg").setExecutor(bg);
//		getCommand("enablewings").setExecutor(bg);
//		Bukkit.getServer().getPluginManager().registerEvents(bg, this);
		// Tiger players
		getCommand("tigerplayer").setExecutor(commandTigerplayer);
		getCommand("uniform").setExecutor(uniformCommand);
		// TAB EXECUTORS
		getCommand("crush").setTabCompleter(new CommandTabCrush(this));
		getCommand("tigerplayer").setTabCompleter(commandTigerplayer);
		getCommand("fuck").setTabCompleter(socialCommands);
		// Registering all players online
		if (Bukkit.getOnlinePlayers().size() != 0) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				tigerListener.register(p);
			}
		}
	}

	@Override
	public void onDisable() {
		log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
		if (ConfigManager.getMainConfiguration().getString("whitelist.url") != "<insert>") {
			WhitelistManager.loadManager();
		}
		for (Entry<UUID, TigerPlayer> entry : TigerPlayer.getPlayers().entrySet()) {
			entry.getValue().saveToPlayersConfig();
		}
		ConfigManager.save();
	}

	public void loadPlugins() {
		// VAULT
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		} else {
			log.info(Utils.announceToChat("Vault economy integration loaded!"));
		}
		ConfigManager.save();
		getLogger().info(Utils.announceToChat("TigerMC v" + version + " loaded"));
		// WORLDGUARD
		Plugin wgplugin = Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
		if (wgplugin == null || !(wgplugin instanceof WorldGuardPlugin)) {
			getLogger().warning(ChatColor.DARK_RED + "Worldguard not loaded! Expect multiple errors!");
		} else {
			wg = (WorldGuardPlugin) wgplugin;
		}
		// SkinsRestorer
		// Connecting to the main SkinsRestorer API
//		try {
//			skinsRestorer = JavaPlugin.getPlugin(SkinsRestorer.class);
//			Console.info(skinsRestorer.toString());
//
//			// Connecting to Bukkit API for applying the skin
//			skinsRestorerBukkitAPI = skinsRestorer.getSkinsRestorerBukkitAPI();
//			Console.info(skinsRestorerBukkitAPI.toString());
//		} catch (Exception e) {
//			Console.warn("Error loading SkinsRestorer");
//			e.printStackTrace();
//		}
	}

	public void loadWhitelist() {
		if (!ConfigManager.getMainConfiguration().isConfigurationSection("whitelist")) {
			ConfigManager.getMainConfiguration().set("whitelist.enabled", false);
			ConfigManager.getMainConfiguration().set("whitelist.url", "<insert>");
			ConfigManager.getMainConfiguration().set("whitelist.column-colleges", 1);
			ConfigManager.getMainConfiguration().set("whitelist.column-name", 2);
			ConfigManager.getMainConfiguration().set("whitelist.update-delay", 5);
			ConfigManager.getMainConfiguration().set("whitelist.message",
					"%player% is not registered! Kindly visit www.tigermc.org to register. If you are not a new player, kindly register again. If you have changed your name, kindly update the GForm.");
			ConfigManager.saveMain();
			Utils.consoleInfo("Created new Whitelist configuration! Please update the file with the appropriate URL.");
		}
		if (ConfigManager.getMainConfiguration().getString("whitelist.url") == "<insert>") {
			Utils.consoleWarn("URL is not yet set! Whitelist won't work!");
			WhitelistManager.setEnabled(false);
		} else {
			if (ConfigManager.getMainConfiguration().getBoolean("whitelist.enabled")) {
				WhitelistManager.loadManager();
				WhitelistScheduler.loadScheduler(this);
				WhitelistManager.setEnabled(true);
			}
		}
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("tigermc")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.hasPermission("tigermc.core.reload") && args.length != 0) {
					Bukkit.getServer().getPluginManager().disablePlugin(this);
					Bukkit.getServer().getPluginManager().enablePlugin(this);
					player.sendMessage(Utils.announceToChat(ChatColor.YELLOW + "TigerMC reloaded!"));
					return true;
				}
				player.sendMessage(Utils.announceToChat("USTMC Plugin v" + version + " by Jus."));
				return true;
			}
		} else if (label.equalsIgnoreCase("createshop")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length != 0 && p.isOp()) {
					p.performCommand("resadmin create UT2-" + args[0]);
					p.performCommand("resadmin server UT2-" + args[0]);
					p.performCommand("resadmin market rentable UT2-" + args[0]);
					p.performCommand("rg define UT2-" + args[0]);
					p.performCommand("rg parent UT2-" + args[0] + " units");
					return true;
				}
			} else {
				sender.sendMessage(Utils.announceToChat("Wait, you're not an OP'd player!"));
			}
		}
		// other commands
		return false;
	}
	// OTHER ON-ENABLE COMMANDS

	// ======================= VAULT =====================

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	public static Economy getEconomy() {
		return econ;
	}

	public static WorldGuardPlugin getWorldGuard() {
		return wg;
	}

	public static main getMainPlugin() {
		return mainInstance;
	}

//	public static SkinsRestorer getSR() {
//		return skinsRestorer;
//	}
//
//	public static SkinsRestorerBukkitAPI getSRBukkit() {
//		return skinsRestorerBukkitAPI;
//	}
	// ===================
}
