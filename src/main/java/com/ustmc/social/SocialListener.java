package com.ustmc.social;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.ustmc.core.main;

public class SocialListener implements Listener {
	main mainPlugin;

	public SocialListener(main plugin) {
		this.mainPlugin = plugin;
	}

	@EventHandler
	public void onStaffChat(AsyncPlayerChatEvent event) {

	}
}
