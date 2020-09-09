package com.ustmc.core;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.ustmc.navigation.LocatorCommand;
import com.ustmc.players.TigerListener;
import com.ustmc.social.SocialCommands;

import net.milkbowl.vault.economy.Economy;

public class main extends JavaPlugin {

	// PLUGINS
	SocialCommands socialCommands;
	LocatorCommand locatorCommand;
	//
	public static String version = "1.03";

	// VAULT INTEGRATION
	private static final Logger log = Logger.getLogger("Minecraft");
	private static Economy econ = null;

	// CONFIGS

	// =========================
	@Override
	public void onEnable() {
		ConfigManager.setPlugin(this);

		socialCommands = new SocialCommands(this);
		locatorCommand = new LocatorCommand(this);
		getCommand("hug").setExecutor(socialCommands);
		getCommand("poke").setExecutor(socialCommands);
		getCommand("crush").setExecutor(socialCommands);
		getCommand("uncrush").setExecutor(socialCommands);
		getCommand("locate").setExecutor(locatorCommand);
		// VAULT
		if (!setupEconomy()) {
			log.severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
			getServer().getPluginManager().disablePlugin(this);
			return;
		} else {
			log.info(Utils.announceToChat("Vault economy integration loaded!"));
		}
		ConfigManager.save();
		socialCommands.restoreCrushes();
		getLogger().info(Utils.announceToChat("TigerMC v" + version + " loaded"));
		// LISTENERS

		Bukkit.getServer().getPluginManager().registerEvents(new TigerListener(this), this);
	}

	@Override
	public void onDisable() {
		log.info(String.format("[%s] Disabled Version %s", getDescription().getName(), getDescription().getVersion()));
		ConfigManager.save();
		socialCommands.saveCrushes();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("tigermc")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				player.sendMessage(Utils.announceToChat("USTMC Plugin v" + version + " by Jus."));
				return true;
			}
		} else if (label.equalsIgnoreCase("createshop")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length != 0 && p.isOp()) {
					p.performCommand("resadmin create UT2-" + args[0]);
					p.performCommand("resadmin setowner UT2-" + args[0] + " UTower2");
					p.performCommand("resadmin market rentable UT2-" + args[0]);
					p.performCommand("rg define UT2-" + args[0]);
					p.performCommand("rg parent UT2-" + args[0] + " units");
					return true;
				}
			} else {
				sender.sendMessage(Utils.announceToChat("Wait, you're not an OP'd player!"));
			}
		} // other commands
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
	// ===================
}
