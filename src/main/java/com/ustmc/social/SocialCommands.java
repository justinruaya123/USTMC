package com.ustmc.social;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.ustmc.core.main;
import com.ustmc.utils.Utils;

public class SocialCommands implements CommandExecutor, TabCompleter {

	main mainPlugin;

	public SocialCommands(main m) {
		this.mainPlugin = m;
	}

	@SuppressWarnings("deprecation")
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
							p.getWorld().spawnParticle(Particle.HEART, p.getEyeLocation(), 10, 1, 1, 1, 0.05);
							target.getWorld().spawnParticle(Particle.HEART, target.getEyeLocation(), 10, 1, 1, 1, 0.05);
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
							target.getWorld().spawnParticle(Particle.SQUID_INK, target.getEyeLocation(), 100, 0.5, 0.5,
									0.5, 0.05);
						} else {
							p.sendMessage(ChatColor.YELLOW + "You nagged yourself!");
							p.getWorld().spawnParticle(Particle.SQUID_INK, p.getEyeLocation(), 100, 0.5, 0.5, 0.5,
									0.05);
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
		} else if (label.equalsIgnoreCase("fuck")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				p.kickPlayer(ChatColor.RED + "The government fk'd us hard already. C'mon...");
			}
		} else if (label.equalsIgnoreCase("sanaol")) {
			if (sender.hasPermission("tigermc.social.sanaol")) {
				if (args.length != 2) {
					sender.sendMessage(Utils.messageUsage("/sanaol <Player1> <Player2"));
					return true;
				}
				Player p1 = Bukkit.getPlayer(args[0]);
				Player p2 = Bukkit.getPlayer(args[1]);
				if (p1 == null || p2 == null) {
					sender.sendMessage(Utils
							.announceToChat(ChatColor.DARK_RED + "One of the players are invalid. Check spelling!"));
				}
				if (p1.getName().equals(p2.getName())) {
					sender.sendMessage(Utils.announceToChat(ChatColor.RED + "Awit ka."));
					return true;
				}
				if (p1.getLocation().distanceSquared(p2.getLocation()) <= 9) {
					p1.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, p1.getLocation(), 50, 2, 2, 2, 0.001);
					p1.playSound(p1.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					p2.playSound(p1.getLocation(), Sound.ENTITY_VILLAGER_NO, 100, 1);
					p1.setVelocity(Utils.vectorFrom(p2.getLocation(), p1.getLocation()).multiply(1.1d).setY(0.7f));
					p2.setVelocity(Utils.vectorFrom(p1.getLocation(), p2.getLocation()).multiply(1.1d).setY(0.7f));
					if (sender instanceof Player) {
						((Player) sender).chat("SANAOL!");
						return true;
					}
				} else {
					sender.sendMessage(Utils.announceToChat(ChatColor.RED + "They're too far :("));
					return true;
				}

			} else {
				sender.sendMessage(Utils.announceToChat(ChatColor.RED + "You do not have permission!"));
				return true;
			}
		}
		return false;
	}
//		else if (label.equalsIgnoreCase("crush")) {
//			if (sender instanceof Player) {
//				Player p = (Player) sender;
//				if (args.length == 0) {
//					sender.sendMessage(Utils.announceToChat("Usage: /crush <add|remove|list> <player>"));
//					return true;
//				} else {
//					if (args[0].equalsIgnoreCase("add")) {
//						UUID crush = TigerPlayer.getUUIDFromName(args[1]);
//					} else if (args[0].equalsIgnoreCase("remove")) {
//
//					} else if (args[0].equalsIgnoreCase("list")) {
//
//					} else {
//						sender.sendMessage(Utils.announceToChat("Usage: /crush <add|remove|list> <player>"));
//						return true;
//					}
//
//					Player crush = Bukkit.getPlayer(args[1]);
//					if (crush != null) {
//						if (!crush.getUniqueId().equals(p.getUniqueId())) {
//							// Crush found
//
//							p.sendMessage(Utils.announceToChat(
//									"You already like " + ChatColor.GOLD + crush.getName() + ChatColor.RED + "!"));
//							return true;
//						} else {
//							sender.sendMessage(Utils.announceToChat("It's nice to love yourself, but no."));
//						}
//					} else {
//						sender.sendMessage(Utils.announceToChat("Your crush must be online for this command to work."));
//					}
//				}
//			} else {
//				sender.sendMessage(Utils.announceToChat("Wait, you're not a player!"));
//			}
//			// ===========================================================================================
//		} else if (label.equalsIgnoreCase("uncrush")) {
//			if (sender instanceof Player) {
//				Player p = (Player) sender;
//				if (args.length != 1) {
//					sender.sendMessage(Utils.announceToChat("Usage: /crush <player>"));
//					return true;
//				} else {
//					Player crush = Bukkit.getPlayer(args[0]);
//					if (crush != null) {
//
//						} else {
//							sender.sendMessage(
//									Utils.announceToChat("I mean... that's just a little sad. Here's a hug uwu"));
//						}
//					} else {
//						sender.sendMessage(Utils.announceToChat("Your crush must be online for this command to work."));
//					}
//				}
//			} else {
//				sender.sendMessage(Utils.announceToChat("Wait, you're not a player!"));
//			}
//		}else if(label.equalsIgnoreCase("anonymousmessage"))
//
//	{
//
//	}return false;
//	}

//	public void checkCrush(Player p, Player crush) {
//		if (crushList.containsKey(crush.getUniqueId())
//				&& crushList.get(crush.getUniqueId()).contains(p.getUniqueId())) {
//			p.sendMessage(Utils.announceToChat("It turns out " + ChatColor.GOLD + crush.getName() + ChatColor.RED
//					+ " has a crush on you, too! <3"));
//			crush.sendMessage(Utils.announceToChat(
//					"Psst! " + ChatColor.GOLD + crush.getName() + ChatColor.RED + " likes you back! <3"));
//		} else {
//			p.sendMessage(Utils.announceToChat(ChatColor.RED + "You like " + ChatColor.GOLD + crush.getName()
//					+ ChatColor.RED + "! Don't worry, they won't know until they like you back!"));
//			p.sendMessage(ChatColor.BLUE + "You can type '" + ChatColor.GOLD + "/anonymousmessage <player> <message>"
//					+ ChatColor.BLUE + "' to send them a discreet message!");
//			crush.sendMessage(ChatColor.RED + "Psst... someone secretly likes you!");
//		}
//	}

	public List<String> uuidToStringArray(HashSet<UUID> array) {
		List<String> elements = new ArrayList<String>();
		for (UUID element : array) {
			elements.add(element.toString());
		}
		return elements;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		List<String> output = new ArrayList<String>();
		if (label.equalsIgnoreCase("fuck")) {
			if (sender instanceof Player) {
				((Player) sender).kickPlayer(ChatColor.RED + "Don't even think about it.");
			}
		}
		return output;
	}

}
