package com.ustmc.core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.ustmc.utils.Utils;

public class CoreCommandListener implements CommandExecutor {

	main mainPlugin;

	public CoreCommandListener(main mainPlugin) {
		this.mainPlugin = mainPlugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("bell")) {
			if (sender instanceof Player) {
				Player p = (Player) sender;
				if (!p.hasPermission("tigermc.admin.bell")) {
					p.sendMessage(Utils.announceToChat("You do not have permission to perform this command!!"));
				}
			}
			World UST = Bukkit.getServer().getWorld("USTMC20077");
			if (UST != null) {
				UST.getBlockAt(-217, 3, -143).setType(Material.REDSTONE_BLOCK);
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "USTMC2077 is not loaded!");
			}
			return true;
		}
		return false;
	}

}
