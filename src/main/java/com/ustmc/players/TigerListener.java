package com.ustmc.players;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;

import com.ustmc.core.main;
import com.ustmc.players.whitelist.WhitelistManager;
import com.ustmc.utils.ConfigManager;
import com.ustmc.utils.Utils;

public class TigerListener implements Listener {
	private main mainPlugin;

	public TigerListener(main mainPlugin) {
		this.setMain(mainPlugin);
	}

	@EventHandler
	public void onWhitelistJoin(AsyncPlayerPreLoginEvent e) {
		if (!WhitelistManager.getWhitelist().containsKey(e.getName()) && WhitelistManager.isEnabled()) {
			e.disallow(Result.KICK_WHITELIST, "§cPlayer §6" + e.getName()
					+ "§c is not whitelisted!\n§bKindly go to §l§6http://www.tigermc.org/§r§b to get whitelisted (UST Mail).\n\n§2If you have recently changed your name, kindly update your response in the Google Forms.");
			return;
		}
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	public void registerPlayer(PlayerJoinEvent e) {
		register(e.getPlayer());
		e.setJoinMessage(
				ChatColor.GREEN + "Player " + ChatColor.GOLD + e.getPlayer().getName() + " joined the server.");
	}

	public void register(Player p) {
		TigerPlayer player = null;
		if (TigerPlayer.getPlayers().containsKey(p.getUniqueId())) {
			p.sendMessage(Utils.announceToChat("Welcome back, " + ChatColor.GOLD + p.getDisplayName()));
			player = TigerPlayer.getPlayers().get(p.getUniqueId());
		} else {
			try {
				player = new TigerPlayer(p);
				p.sendMessage(Utils.announceToChat("Hello there, " + ChatColor.GOLD + p.getName()));
			} catch (Exception exception) {
				p.sendMessage(Utils.announceToChat(
						"Something's wrong about your player data. Kindly consult an administrator immediately!"));
				exception.printStackTrace();
				return;
			}
		}
		String oldName = ConfigManager.getPlayersConfig()
				.getString("data.players." + p.getUniqueId().toString() + ".string.Name");
		if (!p.getName().equals(oldName)) {
			p.sendMessage(Utils.announceToChat(
					ChatColor.GREEN + "You're now known as " + ChatColor.BLUE + p.getName() + ChatColor.GREEN + "!"));
			player.setString("Name", p.getName(), true);
			Bukkit.getServer().broadcastMessage(ChatColor.GREEN + oldName + ChatColor.BLUE + " is now known as "
					+ ChatColor.GOLD + p.getName() + ChatColor.BLUE + "!");
		}
	}

	public main getMain() {
		return mainPlugin;
	}

	public void setMain(main mainPlugin) {
		this.mainPlugin = mainPlugin;
	}
}
