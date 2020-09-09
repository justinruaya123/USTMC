package com.ustmc.social;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import com.ustmc.core.ConfigManager;
import com.ustmc.core.Utils;
import com.ustmc.core.main;

public class SocialCommands implements CommandExecutor {

	main mainPlugin;
	// CRUSHER, CRUSHES
	Map<UUID, HashSet<UUID>> crushList = new HashMap<UUID, HashSet<UUID>>();

	public SocialCommands(main m) {
		this.mainPlugin = m;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("hug")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length != 0) {
					Player target = Bukkit.getPlayer(args[0]);
					if (target != null) {
						if (!(p.getName().equalsIgnoreCase(target.getName()))) {
							p.sendMessage(ChatColor.BLUE + "You're sweet for hugging " + ChatColor.GOLD
									+ target.getName() + ChatColor.BLUE + "!");
							target.sendMessage(ChatColor.RED + "Aww, you've been hugged by " + ChatColor.GOLD
									+ p.getName() + ChatColor.RED + "! <3 <3 <3");
						} else {
							p.sendMessage(ChatColor.RED + "Aww, self-love is important! x");
						}
					} else {
						p.sendMessage(Utils.announceToChat(ChatColor.RED
								+ "The player you want to hug is not on right now, or you may have typed his/her name wrong."));
					}
					return true;
				}
			} else {
				sender.sendMessage(Utils.announceToChat("Wait, you're not a player!"));
			}
		} else if (label.equalsIgnoreCase("poke")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length != 0) {
					Player target = Bukkit.getPlayer(args[0]);
					if (target != null) {
						if (!(p.getName().equalsIgnoreCase(target.getName()))) {
							p.sendMessage(ChatColor.BLUE + "You poked " + ChatColor.GOLD + target.getName()
									+ ChatColor.BLUE + "!");
							target.sendMessage(ChatColor.RED + "Hey! You've been poked by " + ChatColor.GOLD
									+ p.getName() + ChatColor.RED + "!");
						} else {
							p.sendMessage(ChatColor.YELLOW + "You nagged yourself!");
						}
					} else {
						p.sendMessage(Utils.announceToChat(ChatColor.RED
								+ "The player you want to poke is not on right now, or you may have typed his/her name wrong."));
					}
					return true;
				}
			} else {
				sender.sendMessage(Utils.announceToChat("Wait, you're not a player!"));
			}
		} else if (label.equalsIgnoreCase("crush")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length != 1) {
					sender.sendMessage(Utils.announceToChat("Usage: /crush <player>"));
					return true;
				} else {
					Player crush = Bukkit.getPlayer(args[0]);
					if (crush != null) {
						if (!crush.getUniqueId().equals(p.getUniqueId())) {
							if (crushList.containsKey(p.getUniqueId())) {
								HashSet<UUID> uuidList = crushList.get(p.getUniqueId());
								boolean duplicate = false;
								for (UUID cloop : uuidList) {
									if (cloop.equals(crush.getUniqueId())) {
										duplicate = true;
										p.sendMessage(Utils.announceToChat("You already like " + ChatColor.GOLD
												+ crush.getName() + ChatColor.RED + "!"));
										break;
									}
								}
								if (!duplicate) {
									uuidList.add(crush.getUniqueId());
									crushList.put(p.getUniqueId(), uuidList);
									checkCrush(p, crush);
								}

								uuidList = null;
							} else {
								HashSet<UUID> list = new HashSet<UUID>();
								list.add(crush.getUniqueId());
								crushList.put(p.getUniqueId(), list);
								checkCrush(p, crush);
							}
							saveCrushes();
							restoreCrushes();
							return true;
						} else {
							sender.sendMessage(Utils.announceToChat("It's nice to love yourself, but no."));
						}
					} else {
						sender.sendMessage(Utils.announceToChat("Your crush must be online for this command to work."));
					}
				}
			} else {
				sender.sendMessage(Utils.announceToChat("Wait, you're not a player!"));
			}
			// ===========================================================================================
		} else if (label.equalsIgnoreCase("uncrush")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (args.length != 1) {
					sender.sendMessage(Utils.announceToChat("Usage: /crush <player>"));
					return true;
				} else {
					Player crush = Bukkit.getPlayer(args[0]);
					if (crush != null) {
						if (!p.getUniqueId().equals(crush.getUniqueId())) {
							if (crushList.containsKey(p.getUniqueId())) {
								HashSet<UUID> uuidList = crushList.get(p.getUniqueId());
								uuidList.remove(crush.getUniqueId());
								p.sendMessage(Utils.announceToChat("You've removed " + ChatColor.GOLD + crush.getName()
										+ ChatColor.RED + " from your crush list!"));
							}
							return true;
						} else {
							sender.sendMessage(
									Utils.announceToChat("I mean... that's just a little sad. Here's a hug uwu"));
						}
					} else {
						sender.sendMessage(Utils.announceToChat("Your crush must be online for this command to work."));
					}
				}
			} else {
				sender.sendMessage(Utils.announceToChat("Wait, you're not a player!"));
			}
		} else if (label.equalsIgnoreCase("anonymousmessage")) {

		}
		return false;
	}

	public void saveCrushes() {
		Bukkit.getServer().getLogger().info("Attempting to save crushes");
		for (Map.Entry<UUID, HashSet<UUID>> entry : crushList.entrySet()) {
			ConfigManager.getPlayersConfig().set("data.players.crushes." + entry.getKey().toString(),
					uuidToStringArray(entry.getValue()));
		}
		ConfigManager.save();
	}

	public void restoreCrushes() {
		crushList = new HashMap<UUID, HashSet<UUID>>();
		if (ConfigManager.getPlayersConfig().getKeys(false).size() == 0)
			return;
		Bukkit.getServer().getLogger().info("Attempting to load crushes");
		try {
			ConfigurationSection section = ConfigManager.getPlayersConfig()
					.getConfigurationSection("data.players.crushes");
			Set<String> index = section.getKeys(false);
			for (String s : index) {
				UUID admirer = UUID.fromString(s);
				List<String> crushes = ConfigManager.getPlayersConfig().getStringList("data.players.crushes." + s);
				HashSet<UUID> crushFinal = new HashSet<UUID>();
				for (String crush : crushes) {
					crushFinal.add(UUID.fromString(crush));
				}
				crushList.put(admirer, crushFinal);
			}
		} catch (Exception e) {

		}
		Bukkit.getServer().getLogger().info("[TigerMC] loaded " + crushList.keySet().size() + " players with crushes");
	}

	public void checkCrush(Player p, Player crush) {
		if (crushList.containsKey(crush.getUniqueId())
				&& crushList.get(crush.getUniqueId()).contains(p.getUniqueId())) {
			p.sendMessage(Utils.announceToChat("It turns out " + ChatColor.GOLD + crush.getName() + ChatColor.RED
					+ " has a crush on you, too! <3"));
			crush.sendMessage(Utils.announceToChat(
					"Psst! " + ChatColor.GOLD + crush.getName() + ChatColor.RED + " likes you back! <3"));
		} else {
			p.sendMessage(Utils.announceToChat(ChatColor.RED + "You like " + ChatColor.GOLD + crush.getName()
					+ ChatColor.RED + "! Don't worry, they won't know until they like you back!"));
			p.sendMessage(ChatColor.BLUE + "You can type '" + ChatColor.GOLD + "/anonymousmessage <player> <message>"
					+ ChatColor.BLUE + "' to send them a discreet message!");
			crush.sendMessage(ChatColor.RED + "Psst... someone secretly likes you!");
		}
	}

	public List<String> uuidToStringArray(HashSet<UUID> array) {
		List<String> elements = new ArrayList<String>();
		for (UUID element : array) {
			elements.add(element.toString());
		}
		return elements;
	}

}
