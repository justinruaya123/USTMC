package com.ustmc.players;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.ustmc.core.main;
import com.ustmc.utils.Utils;

public class CommandTigerplayer implements CommandExecutor, TabCompleter {

	private main mainPlugin;

	public CommandTigerplayer(main mainPlugin) {
		this.mainPlugin = mainPlugin;
	}

	public main getPlugin() {
		return mainPlugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		ArrayList<String> output = new ArrayList<String>();
		if (label.equalsIgnoreCase("tigerplayer")) {

			if (args.length == 1) {
				output.add("attributes");
				return output;
			}
			if (args.length == 2) {
				for (String index : TigerPlayer.getIndexedPlayers().keySet()) {
					if (args[1].length() == 0) {
						output.add(index);
					} else if (index.toLowerCase().startsWith(args[1].toLowerCase())) {
						output.add(index);
					}
				}
				output.add("me");
				return output;
			}
			if (args.length == 3) {
				output.add("read");
				output.add("set");
				output.add("remove");
				return output;
			}
			if (args.length == 4) {
				output.add("string");
				output.add("boolean");
				output.add("crush");
				output.add("double");
				output.add("integer");
				return output;
			}
		}
		return output;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// Command: /tigerplayer <attributes> <player|me> <set|remove|read> <dataType>
		// <dataName>
		// (args)
		// <value>
		if (label.equalsIgnoreCase("tigerplayer")) {
			if (!(sender.hasPermission("tigermc.player.tigerplayer") || sender.isOp())) {
				sender.sendMessage(Utils
						.announceToChat(ChatColor.DARK_RED + "You do not have perissions to perform this command!"));
				return true;
			}
			if (args.length >= 5) {
				if (args[0].equalsIgnoreCase("attributes")) {
					if (args[1].equalsIgnoreCase("me")) {
						if (sender instanceof Player) {
							args[1] = sender.getName();
							return runAttributes(sender, cmd, args, ((Player) sender).getUniqueId());
						} else {
							sender.sendMessage(Utils.announceToChat(ChatColor.RED + "You're not a player!"));
							return true;
						}
					} else {
						Player playerTarget = Bukkit.getPlayer(args[1]);
						if (playerTarget == null) {
							sender.sendMessage(Utils.announceToChat(ChatColor.RED + "Unknown player!"));
							return true;
						}
						args[1] = playerTarget.getName();
						return runAttributes(sender, cmd, args, playerTarget.getUniqueId());
					}

				} // End tigerplay args[0]
			}
		}
		return false;
	}

	public boolean runAttributes(CommandSender sender, Command cmd, String[] args, UUID player) {
		if (args[2].equalsIgnoreCase("set")) {

		} else if (args[2].equalsIgnoreCase("remove")) {

		} else if (args[2].equalsIgnoreCase("read")) {

		} else {
			sender.sendMessage(Utils.announceToChat(ChatColor.RED + "Invalid arguments!"));
			return true;
		}
		return false;
	}

}
