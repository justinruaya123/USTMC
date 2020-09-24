package com.ustmc.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.flags.StateFlag.State;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.ustmc.utils.ConfigManager;
import com.ustmc.utils.Utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class BuildingGames implements CommandExecutor, TabCompleter, Listener, Runnable {

	main mainPlugin = null;
	boolean started = false;
	static RegionManager regions = null;
	public static HashSet<String> plots = null;
	ItemStack flapItem = null;
	HashMap<UUID, Long> canFlap;

	public BuildingGames(main plugin) {
		this.mainPlugin = plugin;
		regions = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer()
				.get(BukkitAdapter.adapt(Bukkit.getWorld("Minigames")));
		plots = new HashSet<String>();

		flapItem = new ItemStack(Material.FEATHER);
		ItemMeta flapItemMeta = flapItem.getItemMeta();
		flapItemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "Flap");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.BLUE + "Gifted by Flappy, it allows anyone to flap.");
		lore.add(ChatColor.YELLOW + "Right click" + ChatColor.AQUA + " to flap.");
		flapItemMeta.setLore(lore);
		flapItem.setItemMeta(flapItemMeta);
		canFlap = new HashMap<UUID, Long>();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> returnString = new ArrayList<String>();
		if (label.equalsIgnoreCase("createblock")) {
			if (sender instanceof Player & args.length > 0) {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
						returnString.add(p.getName());
					}
				}
				returnString.add("<empty>");
			}
		}
		return returnString;
	}

	@SuppressWarnings("unused")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("createblock")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("You need to be a player!");
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp()) {
				String select, select2;
				if (args.length == 0) {
					select = "<AVAILABLE>";
				} else {
					if (args[0].equalsIgnoreCase("<empty>")) {
						select = "<AVAILABLE>";
					} else {
						Player create = Bukkit.getPlayer(args[0]);
						if (create == null) {
							player.sendMessage(Utils.announceToChat(
									"Unknown player! Make sure they are online or their name was spelled correctly! If you want to create an empty cell, either put '<empty>' or leave it blank."));
							return false;
						}
						select = create.getName();
					}
				}
				select2 = (args.length > 1) ? args[0] : plots.size() + "";
				int x = player.getLocation().getBlockX(), y = player.getLocation().getBlockY(),
						z = player.getLocation().getBlockZ();
				String index = "plot-" + select2; // plot-<ID>
				ConfigManager.getMainConfiguration().set("custom.BuildingGames." + index, select);

				ProtectedRegion newPlot = new ProtectedCuboidRegion(index, BlockVector3.at(x, y - 1, z),
						BlockVector3.at(x + 25, y + 23, z + 27));

				if (!isAvailable(select)) {
					newPlot.getMembers().addPlayer(select);
				}

				newPlot.setPriority(5);
				newPlot.setFlag(Flags.BUILD, StateFlag.State.ALLOW);
				newPlot.setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
				newPlot.setFlag(Flags.USE, StateFlag.State.ALLOW);
				newPlot.setFlag(Flags.USE.getRegionGroupFlag(), RegionGroup.MEMBERS);
				newPlot.setFlag(main.PLOT_AVAILABLE, State.ALLOW);

				plots.add(index);
				BuildingGames.regions.addRegion(newPlot);
				player.sendMessage(Utils.announceToChat("Created build plot with ID " + select2));

			} else {
				player.sendMessage(Utils.announceToChat("You don't have permissions for this command!"));
				return true;
			}
		} else if (label.equalsIgnoreCase("generateplots")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				final UUID puuid = player.getUniqueId();
				if (player.isOp()) {
					int ctr = 0;
					for (int x = 0; x < 329; x += 41) {
						for (int z = 0; z < 345; z += 43) { // x offset = 41; z offset = 43
							final double fx = x;
							final double fz = z;
							final int passCtr = ctr;
							Bukkit.getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {

								@Override
								public void run() {
									Player p = Bukkit.getPlayer(puuid);
									Location pl = new Location(p.getWorld(), p.getLocation().getBlockX() + fx,
											(double) p.getLocation().getBlockY(), p.getLocation().getBlockZ() + fz);
									p.teleport(pl);
									p.sendMessage(ChatColor.GOLD + "" + passCtr + " >> " + ChatColor.BLUE
											+ "Teleported to: " + pl.getX() + "" + pl.getY() + "" + pl.getZ());
								}
							}, 5 * ctr++);
						}
					}
				} else {
					player.sendMessage(Utils.announceToChat("You don't have permissions for this command!"));
					return true;
				}
			}

		}
		return false;
	}

	public static void initialize() {
		if (ConfigManager.getMainConfiguration().isConfigurationSection("custom.BuildingGames")) {
			for (String str : ConfigManager.getMainConfiguration().getConfigurationSection("custom.BuildingGames") // STR
																													// =
																													// plot-<ID>
					.getKeys(false)) {
				ProtectedRegion rg = BuildingGames.regions.getRegion(str);
				plots.add(rg.getId());
			}
		} else {
			ConfigManager.getMainConfiguration().createSection("custom.BuildingGames");
		}
	}

	public static void save() {
		for (String index : plots) {
			ProtectedRegion rg = BuildingGames.regions.getRegion(index);
			DefaultDomain members = rg.getMembers();
			if (members.size() != 0) {
				ConfigManager.getMainConfiguration().set("custom.BuildingGames." + rg.getId(), members.getPlayers()); //
			}
		}
	}

	public boolean isAvailable(String test) {
		return test == "<AVAILABLE>";
	}

	public String getPlotIndex(String index) {
		return "custom.BuildingGames." + index;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@EventHandler
	public void onDoubleJump(PlayerInteractEvent e) {
		Player caster = e.getPlayer();
		if (caster.getInventory().getItemInMainHand() != null) {
			ItemStack main = caster.getInventory().getItemInMainHand();
			if (main.getType() == Material.FEATHER
					&& main.getItemMeta().getLore().equals(flapItem.getItemMeta().getLore())) {

				if (canFlap.get(caster.getUniqueId()) == 0L) {
					caster.playSound(caster.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 5, 0.8f);
					caster.setFallDistance(0.0f);
					Vector direction = caster.getLocation().getDirection();
					direction.multiply(0.25f);
					direction.setY(1.4);

					caster.setVelocity(direction);
					caster.getWorld().spawnParticle(Particle.CLOUD, caster.getLocation(), 100);
					canFlap.put(caster.getUniqueId(), System.currentTimeMillis() + 4 * 1000);
					final UUID casterUUID = caster.getUniqueId();
					Bukkit.getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {

						@Override
						public void run() {
							canFlap.put(casterUUID, 0L);

						}
					}, 20 * 4);

				} else {
					double round = Math.round(
							((double) System.currentTimeMillis() - canFlap.get(caster.getUniqueId()) / 10)) / 100.0;
					caster.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.RED
							+ "You must wait" + ChatColor.YELLOW + round + ChatColor.RED + " before flapping."));
				}

			}
		}
	}

}
