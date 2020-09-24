package com.ustmc.social;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ustmc.core.main;
import com.ustmc.players.TigerPlayer;
import com.ustmc.utils.Utils;

public class SocialCommands implements CommandExecutor {

	main mainPlugin;

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
				if (args.length == 0) {
					sender.sendMessage(Utils.announceToChat("Usage: /crush <add|remove|list> <player>"));
					return true;
				} else {
					if (args[0].equalsIgnoreCase("add")) {
						UUID crush = TigerPlayer.getUUIDFromName(args[1]);
					} else if (args[0].equalsIgnoreCase("remove")) {

					} else if (args[0].equalsIgnoreCase("list")) {

					} else {
						sender.sendMessage(Utils.announceToChat("Usage: /crush <add|remove|list> <player>"));
						return true;
					}

					Player crush = Bukkit.getPlayer(args[1]);
					if (crush != null) {
						if (!crush.getUniqueId().equals(p.getUniqueId())) {
							// Crush found

							p.sendMessage(Utils.announceToChat(
									"You already like " + ChatColor.GOLD + crush.getName() + ChatColor.RED + "!"));
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
