package com.ustmc.social;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import com.ustmc.core.main;
import com.ustmc.utils.Utils;

public class SocialListener implements Listener, Runnable {
	main mainPlugin;

	public SocialListener(main plugin) {
		this.mainPlugin = plugin;
		Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, this, 5L, 5L);
	}

	@EventHandler
	public void onStaffChat(AsyncPlayerChatEvent event) {

	}

	@EventHandler
	public void onKiss(PlayerToggleSneakEvent e) {
		if (!(e.isSneaking())) {
			return;
		}
		Player kissed = null;
		// double debugDistance = 0;
		for (Entity ent : e.getPlayer().getEyeLocation().getChunk().getEntities()) {
			if (ent instanceof Player) {
				Player player = (Player) ent;
				if (player.getUniqueId().equals(e.getPlayer().getUniqueId()))
					continue;
				double d2 = e.getPlayer().getEyeLocation()
						.add(e.getPlayer().getEyeLocation().getDirection().multiply(0.3d))
						.distanceSquared(player.getEyeLocation());
				if (d2 < 0.1) {
					kissed = player;
					break;
				}
			}
		}
		if (kissed == null) {
			return;
		}
		if (e.getPlayer().getEyeLocation().getDirection().dot(kissed.getEyeLocation().getDirection()) <= -0.80) {

			e.getPlayer().sendMessage(
					Utils.announceToChat(ChatColor.RED + "You've kissed " + ChatColor.BLUE + kissed.getName()));
			kissed.sendMessage(Utils.announceToChat(
					ChatColor.RED + "You've been kissed by " + ChatColor.BLUE + e.getPlayer().getName()));
			e.getPlayer().getWorld().spawnParticle(Particle.HEART, e.getPlayer().getEyeLocation(), 10, 0.5, 0.2, 0.5, 0.005);
		}

	}

	@Override
	public void run() {
		Player p = Bukkit.getPlayer("FlamesOfWar2543");
		if (p == null) {
			return;
		}
		// p.getWorld().spawnParticle(Particle.FLAME,
		// p.getEyeLocation().add(p.getEyeLocation().getDirection().multiply(0.3d)), 1,
		// 0, 0, 0, 0.0);
	}

}
