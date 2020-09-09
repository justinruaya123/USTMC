package com.ustmc.navigation;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.ustmc.core.ConfigManager;
import com.ustmc.core.Utils;
import com.ustmc.core.main;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LocatorCommand implements CommandExecutor, Runnable {

	main mainPlugin;
	HashMap<UUID, UUID> locating = new HashMap<UUID, UUID>();

	public LocatorCommand(main main) {
		this.mainPlugin = main;
		long intervalTicks = 20L;
		try {
			intervalTicks = ConfigManager.getMainConfiguration().getLong("locator.interval");
		} catch (Exception e) {
			Bukkit.getLogger().info(
					Utils.announceToChat("Error loading intervals for Locator utilities. Using intervalTicks = 20."));
			intervalTicks = 20L;
			ConfigManager.getMainConfiguration().set("locator.interval", 20);
			ConfigManager.saveMain();
		}
		Bukkit.getScheduler().scheduleSyncRepeatingTask(mainPlugin, this, 0L, intervalTicks);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("locate")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length != 0) {
					Player target = Bukkit.getPlayer(args[0]);
					if (target.getUniqueId().equals(p.getUniqueId())) {
						p.sendMessage(ChatColor.DARK_RED + "You're here...");
						return true;
					}
					p.sendMessage(Utils.announceToChat("Currently locating " + ChatColor.GOLD + target.getName()
							+ ChatColor.LIGHT_PURPLE
							+ ". Your compass target is set to that player as long as it's named 'Locator Compass'."));
					locating.put(p.getUniqueId(), target.getUniqueId());
					return true;
//					if (target != null) {
//						double bal = main.getEconomy().getBalance((OfflinePlayer) p, p.getWorld().getName());
//						if (bal >= 100) {
//							main.getEconomy().withdrawPlayer((OfflinePlayer) p, 100);
//							p.sendMessage(Utils
//									.announceToChat("Transaction successful! Php 100 was deducted from your account."));
//						} else {
//							p.sendMessage(Utils.announceToChat("You do not have enough funds."));
//
//						}
//					} else {
//						p.sendMessage(Utils.announceToChat(ChatColor.RED
//								+ "The player may not be online, or you may have typed his/her name wrong."));
//					}
				} else {
					p.sendMessage(Utils.announceToChat("Invalid player! Make sure they're online!"));
				}
			} else {
				sender.sendMessage(Utils.announceToChat("Wait, you're not a player!"));
			}
		}
		return false;
	}

	@Override
	public void run() {
		// FOR LOCATOR
		if (locating.keySet().size() != 0) {
			for (UUID uuid : locating.keySet()) {
				Player locator = Bukkit.getPlayer(uuid);
				if (!locator.isOnline() || locator.isEmpty()) {
					locating.remove(uuid);
					continue;
				}
				ItemStack main = locator.getInventory().getItemInMainHand();
				ItemStack offHand = locator.getInventory().getItemInMainHand();
				if ((main.getType().equals(Material.COMPASS)
						&& main.getItemMeta().getDisplayName() == "Locating Compass")
						|| (offHand.getType().equals(Material.COMPASS)
								&& offHand.getItemMeta().getDisplayName() == "Locating Compass")) {
					Player target = Bukkit.getPlayer(locating.get(uuid));
					if (target.isOnline()) {
						locator.spigot().sendMessage(ChatMessageType.ACTION_BAR,
								new TextComponent(ChatColor.GOLD + target.getName() + ChatColor.GREEN + " is "
										+ locator.getLocation().distance(target.getLocation()) + " blocks away."));
						locator.setCompassTarget(target.getLocation());
					} else {
						target.spigot().sendMessage(ChatMessageType.ACTION_BAR,
								new TextComponent(ChatColor.RED + target.getName() + " is offline."));
					}

				}
			}
		}
		// FOR SOC DIST

	}

	public boolean isHoldingLocatorCompass(PlayerInventory inventory) {
		
		return false;
	}

}
