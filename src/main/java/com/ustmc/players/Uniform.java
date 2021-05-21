package com.ustmc.players;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.persistence.PersistentDataType;

import com.ustmc.core.main;
import com.ustmc.utils.Utils;

public class Uniform implements CommandExecutor, Listener {

	main mainPlugin;

	public Uniform(main mainPlugin) {
		this.mainPlugin = mainPlugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (label.equalsIgnoreCase("uniform")) {
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(Utils.announceToChat(ChatColor.RED
							+ "You're not a player! Non-players need to use /uniform <Player> <College> <Type>"));
					return true;
				} else {
					Player p = (Player) sender;
					College col = College
							.getCollegeComplete(TigerPlayer.getPlayers().get(p.getUniqueId()).getString("College"));

					p.openInventory(col.getInventory());
					p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_PLACE, 100f, 0.5f);
					return true;
				}
			} else {
				if (sender.hasPermission("tigermc.admin.uniform") || sender.isOp() || !(sender instanceof Player)) {
					Player p = Bukkit.getPlayer(args[0]);
					if (p == null) {
						sender.sendMessage(
								Utils.messageUsage("/uniform <PlayerName | @all | @students> <Wear | Remove>"));
						return true;
					}
					// Logic
				}
			}
			// Start logic in opening uniform
		}
		return false;
	}

	@EventHandler
	public void changeUniform(InventoryClickEvent e) {
		if (e.getCurrentItem() == null)
			return;
		if (e.getCurrentItem().getItemMeta() == null)
			return;
		if (e.getView().getTitle() == null)
			return;
		if (College.isInventory(e.getView())) {
			Player p = (Player) e.getWhoClicked();
			if (e.getCurrentItem().getType() == Material.BARRIER) {
				p.sendMessage(e.getCurrentItem().getItemMeta().getDisplayName());
				Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "skin clear " + p.getName());
				p.sendMessage(Utils.announceToChat(
						"You have taken your uniform off. If nothing happened, it means you're not using the Java Edition (PC)."));
				p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 100f, 0.5f);
				p.addAttachment(main.mainInstance, "group.studentid", false);
				p.sendTitle(ChatColor.RED + "You are no longer wearing your uniform.",
						ChatColor.GREEN + "You can no longer enter buildings.", 20, 60, 20);

			} else if (e.getCurrentItem().getType() == Material.LEATHER_CHESTPLATE) {
				if (e.getCurrentItem().getItemMeta().getPersistentDataContainer().has(Utils.unifKey,
						PersistentDataType.STRING)) {
					Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(),
							"skin set " + p.getName() + " " + e.getCurrentItem().getItemMeta()
									.getPersistentDataContainer().get(Utils.unifKey, PersistentDataType.STRING));
					p.playSound(p.getLocation(), Sound.ITEM_ARMOR_EQUIP_LEATHER, 100, 1);
					p.addAttachment(main.mainInstance, "group.studentid", true);
					p.sendTitle(ChatColor.YELLOW + "You are now wearing your uniform.",
							ChatColor.GREEN + "You can now enter buildings.", 20, 60, 20);
				}

			}
			e.setCancelled(true);
			e.getView().close();
		}
	}

}
