package com.ustmc.players.whitelist;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import com.ustmc.core.main;
import com.ustmc.utils.ConfigManager;

public class WhitelistScheduler implements Runnable {
	private static int taskid = 0;
	static main mainPlugin;

	public static void loadScheduler(main mainPlugin) {
		WhitelistScheduler.mainPlugin = mainPlugin;
		BukkitTask tick = Bukkit.getServer().getScheduler().runTaskTimerAsynchronously(mainPlugin,
				new WhitelistScheduler(), 20L,
				1200 * ConfigManager.getMainConfiguration().getInt("whitelist.update-delay"));
		taskid = tick.getTaskId();
	}

	public static int getTaskID() {
		return taskid;
	}

	public static void setTaskID(final int value) {
		WhitelistScheduler.taskid = value;
	}

	@Override
	public void run() {
		if (WhitelistManager.isEnabled()) {
			WhitelistManager.downloadSheets();
		}
	}
}
