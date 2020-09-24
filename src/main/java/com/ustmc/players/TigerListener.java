package com.ustmc.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.ustmc.core.main;
import com.ustmc.utils.ConfigManager;
import com.ustmc.utils.Utils;

import net.md_5.bungee.api.ChatColor;

public class TigerListener implements Listener {
	private main mainPlugin;

	public TigerListener(main mainPlugin) {
		this.setMain(mainPlugin);
	}

	@EventHandler
	public void registerPlayer(PlayerJoinEvent e) {
		TigerPlayer player = null;
		if (TigerPlayer.getPlayers().containsKey(e.getPlayer().getUniqueId())) {
			e.getPlayer().sendMessage(
					Utils.announceToChat("Welcome back, " + ChatColor.GOLD + e.getPlayer().getDisplayName()));
			player = TigerPlayer.getPlayers().get(e.getPlayer().getUniqueId());
		} else {
			try {
				player = new TigerPlayer(e.getPlayer());
				e.getPlayer()
						.sendMessage(Utils.announceToChat("Hello there, " + ChatColor.GOLD + e.getPlayer().getName()));
			} catch (Exception exception) {
				e.getPlayer().sendMessage(Utils.announceToChat(
						"Something's wrong about your player data. Kindly consult an administrator immediately!"));
				exception.printStackTrace();
				return;
			}
		}
		String oldName = ConfigManager.getPlayersConfig()
				.getString("data.players." + e.getPlayer().getUniqueId().toString() + ".string.Name");
		if (e.getPlayer().getName() != oldName) {
			e.getPlayer().sendMessage(Utils.announceToChat(ChatColor.GREEN + "You're now known as " + ChatColor.BLUE
					+ e.getPlayer().getName() + ChatColor.GREEN + "!"));
			player.setString("Name", e.getPlayer().getName(), true);
		}

	}

	public main getMain() {
		return mainPlugin;
	}

	public void setMain(main mainPlugin) {
		this.mainPlugin = mainPlugin;
	}
}
