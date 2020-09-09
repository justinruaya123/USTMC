package com.ustmc.players;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.ustmc.core.main;

public class TigerListener implements Listener {
	private main mainPlugin;

	public TigerListener(main mainPlugin) {
		this.setMain(mainPlugin);
	}

	@EventHandler
	public void registerPlayer(PlayerJoinEvent e) {
		e.getPlayer().kickPlayer("lol");
	}

	public main getMain() {
		return mainPlugin;
	}

	public void setMain(main mainPlugin) {
		this.mainPlugin = mainPlugin;
	}
}
