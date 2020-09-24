package com.ustmc.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.registry.FlagConflictException;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.ustmc.navigation.LocatorCommand;
import com.ustmc.players.TigerListener;
import com.ustmc.social.SocialCommands;
import com.ustmc.utils.ConfigManager;
import com.ustmc.utils.Utils;

import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;

public class main extends JavaPlugin {

	// PLUGINS
	SocialCommands socialCommands;
	LocatorCommand locatorCommand;
	//
	public static String version = "1.10";

	// VAULT INTEGRATION
	private static final Logger log = Logger.getLogger("Minecraft");
	private static Economy econ = null;

	// PLUGINS
	private static WorldGuardPlugin wg;
	public static StateFlag PLOT_AVAILABLE;

	// =========================
	@Override
	public void onEnable() {
		Bukkit.getPluginManager().addPermission(new Permission("tigermc.reload"));
		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		ConfigManager.setPlugin(this);

		socialCommands = new SocialCommands(this);
		locatorCommand = new LocatorCommand(this);
		// LISTENERS
		// Bukkit.getServer().getPluginManager().registerEvents(new
		// SocialListener(this), this);
		Bukkit.getServer().getPluginManager().registerEvents(new TigerListener(this), this);
		//
		getCommand("hug").setExecutor(socialCommands);
		getCommand("poke").setExecutor(socialCommands);
		// getCommand("crush").setExecutor(socialCommands);
		// getCommand("uncrush").setExecutor(socialCommands);
		// getCommand("trackplayer").setExecutor(locatorCommand);
		BuildingGames bg = new BuildingGames(this);
		getCommand("createblock").setExecutor(bg);
		getCommand("generateplots").setExecutor(bg);
		getCommand("joinhg").setExecutor(bg);
		Bukkit.getServer().getPluginManager().registerEvents(bg, this);
		// TAB EXECUTORS
		// getCommand("crush").setTabCompleter(new CommandTabCrush(this));

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
		FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();
		try {
			// create a flag with the name "my-custom-flag", defaulting to true
			StateFlag flag = new StateFlag("plot-available", true);
			registry.register(flag);
			main.PLOT_AVAILABLE = flag; // only set our field if there was no error
		} catch (FlagConflictException e) {
			// some other plugin registered a flag by the same name already.
			// you can use the existing flag, but this may cause conflicts - be sure to
			// check type
			Flag<?> existing = registry.get("plot-available");
			if (existing instanceof StateFlag) {
				main.PLOT_AVAILABLE = (StateFlag) existing;
			} else {
				// types don't match - this is bad news! some other plugin conflicts with you
				// hopefully this never actually happens
			}
		}
		// LISTENERS

		Bukkit.getServer().getPluginManager().registerEvents(new TigerListener(this), this);
	}

	@Override
	public void onDisable() {
		log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
		ConfigManager.save();
	}

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
	// ===================
}
