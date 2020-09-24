package com.ustmc.tabs;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.ustmc.core.main;

public class CommandTabCrush implements TabCompleter {

	main plugin;

	public CommandTabCrush(main mainPlugin) {
		this.plugin = mainPlugin;
	}

	@Override
	public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		// TODO Auto-generated method stub
		return null;
	}

}
