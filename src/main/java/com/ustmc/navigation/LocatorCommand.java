package com.ustmc.navigation;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.ustmc.core.main;
import com.ustmc.utils.ConfigManager;
import com.ustmc.utils.Utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class LocatorCommand implements CommandExecutor, Runnable {

	main mainPlugin;
	HashMap<UUID, UUID> locating;

	public LocatorCommand(main main) {
		this.mainPlugin = main;
		long intervalTicks = 20L;
		try {
			intervalTicks = ConfigManager.getMainConfiguration().getLong("locator.interval");
			if (intervalTicks <= 0) {
				Bukkit.getLogger().info(Utils
						.announceToChat("Error loading intervals for Locator utilities. Using intervalTicks = 20."));
				intervalTicks = 20L;
				ConfigManager.getMainConfiguration().set("locator.interval", 20);
				ConfigManager.saveMain();
			}
		} catch (Exception e) {
			Bukkit.getLogger().info(
					Utils.announceToChat("Error loading intervals for Locator utilities. Using intervalTicks = 20."));
			intervalTicks = 20L;
			ConfigManager.getMainConfiguration().set("locator.interval", 20);
			ConfigManager.saveMain();
		}
		locating = new HashMap<UUID, UUID>();
		Bukkit.getScheduler().scheduleSyncRepeatingTask(mainPlugin, this, 0L, intervalTicks);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (label.equalsIgnoreCase("trackplayer")) {
			if (sender instanceof Player) {
				sender.sendMessage(Utils.announceToChat("Wait, you're not a player!"));
				return false;
			}
			Player p = (Player) sender;
			p.set
			if (args.length != 0) {
				double maxDistance = 0;
				UUID farthestPlayer = null;
				if (args[0] == "@random") {
					for (Player player : Bukkit.getOnlinePlayers()) {
						double dsquared = p.getLocation().distanceSquared(player.getLocation());
						if (dsquared > maxDistance) {
							farthestPlayer = player.getUniqueId();
							maxDistance = dsquared;
						}
					}
					Player target = Bukkit.getPlayer(farthestPlayer);
					if (target.getUniqueId().equals(p.getUniqueId())) {
						p.sendMessage(ChatColor.DARK_RED + "You're here...");
						return true;
					}
					p.sendMessage(Utils.announceToChat("Currently locating " + ChatColor.GOLD + target.getName()
							+ ChatColor.LIGHT_PURPLE
							+ ". Your compass target is set to that player as long as it's named 'Locator Compass'."));
					locating.put(p.getUniqueId(), target.getUniqueId());

				} else {
					Player target = Bukkit.getPlayer(args[0]);
					if (target == null) {
						p.sendMessage(Utils
								.announceToChat("Invalid person! Make sure you're typing his/her username right."));
						return true;
					}
					if (target.getUniqueId().equals(p.getUniqueId())) {
						p.sendMessage(ChatColor.DARK_RED + "You're here...");
						return true;
					}
					p.sendMessage(Utils.announceToChat("Currently locating " + ChatColor.GOLD + target.getName()
							+ ChatColor.LIGHT_PURPLE
							+ ". Your compass target is set to that player as long as it's named 'Locator Compass'."));
					locating.put(p.getUniqueId(), target.getUniqueId());
					return true;
				}
			} else {
				p.sendMessage(Utils.announceToChat("Usage: " + ChatColor.AQUA + "/trackplayer <username>"));
			}

		}
		return false;
	}

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

	@Override
	public void run() {
		// FOR LOCATOR
		if (locating.keySet().size() != 0) {
			for (UUID uuid : locating.keySet()) {
				Player locator = Bukkit.getPlayer(uuid);
				try {
					if (locator == null || !locator.isOnline()) {
						locating.remove(uuid);
						continue;
					}
				} catch (NullPointerException e) {
					locating.remove(uuid);
					continue;
				}

				if (isHoldingLocatorCompass(locator.getInventory())) {
					Player target = Bukkit.getPlayer(locating.get(uuid));
					if (target != null) {
						if (locator.getLocation().distanceSquared(target.getLocation()) <= 4) {
							locator.playSound(locator.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 10, 1);
							locator.sendMessage(ChatColor.GOLD + "You found " + ChatColor.AQUA + target.getName()
									+ ChatColor.GOLD + "!");
							target.sendMessage(ChatColor.AQUA + locator.getName() + ChatColor.GOLD
									+ " found you using a Locator Compass!");
							locating.remove(locator.getUniqueId());
						}
						locator.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD
								+ target.getName() + ChatColor.GREEN + " is "
								+ (float) (Math.round(locator.getLocation().distance(target.getLocation()) * 100)) / 100
								+ " blocks away."));
						locator.setCompassTarget(target.getLocation());
						// locator.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 0,
						// 1));
						// locator.addPotionEffect(
						// new PotionEffect(PotionEffectType.NIGHT_VISION, 20, 1, true, false, false));
						locator.addPotionEffect(
								new PotionEffect(PotionEffectType.BLINDNESS, 20, 1, true, false, false));
					}
				} else {
					OfflinePlayer targetOffline = Bukkit.getOfflinePlayer(locating.get(uuid));
					if (targetOffline != null) {
						locator.spigot().sendMessage(ChatMessageType.ACTION_BAR,
								new TextComponent(ChatColor.RED + targetOffline.getName() + " is offline."));
					}
				}

			}
		}
	}
	// FOR SOC DIST

	public boolean isHoldingLocatorCompass(PlayerInventory inventory) {
		ItemStack main = inventory.getItemInMainHand();
		if (main != null) {
			if (main.getType() == Material.COMPASS
					&& ChatColor.stripColor(main.getItemMeta().getDisplayName()).equalsIgnoreCase("Locator Compass")) {
				return true;
			}
		}
		ItemStack offHand = inventory.getItemInOffHand();
		if (offHand != null) {
			if (offHand.getType() == Material.COMPASS && ChatColor.stripColor(offHand.getItemMeta().getDisplayName())
					.equalsIgnoreCase("Locator Compass")) {
				return true;
			}
		}
		return false;
	}

}
