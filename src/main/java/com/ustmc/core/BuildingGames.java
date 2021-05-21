package com.ustmc.core;


public class BuildingGames implements CommandExecutor, TabCompleter, Listener, Runnable {
/*
	main mainPlugin = null;
	boolean started = false;
	int timer = 0;
	static RegionManager regions = null;
	ItemStack flapItem = null;
	HashMap<UUID, Long> canFlap;
	public static HashMap<String, String> allPlots;
	int taskId = -1;
	boolean startedHG = false;
	HashMap<UUID, Long> lastHit;

	public BuildingGames(main plugin) {
		this.mainPlugin = plugin;
		regions = com.sk89q.worldguard.WorldGuard.getInstance().getPlatform().getRegionContainer()
				.get(BukkitAdapter.adapt(Bukkit.getWorld("Minigames")));

		flapItem = new ItemStack(Material.FEATHER);
		ItemMeta flapItemMeta = flapItem.getItemMeta();
		flapItemMeta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "Flap");
		ArrayList<String> lore = new ArrayList<String>();
		lore.add(ChatColor.BLUE + "Gifted by Flappy, it allows anyone to flap.");
		lore.add(ChatColor.YELLOW + "Right click" + ChatColor.AQUA + " to flap.");
		flapItemMeta.setLore(lore);
		flapItem.setItemMeta(flapItemMeta);
		canFlap = new HashMap<UUID, Long>();
		allPlots = new HashMap<String, String>();
		lastHit = new HashMap<UUID, Long>();
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
		List<String> returnString = new ArrayList<String>();
		if (label.equalsIgnoreCase("createblock")) {
			if (sender instanceof Player & args.length > 0) {
				for (Player p : Bukkit.getServer().getOnlinePlayers()) {
					if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
						returnString.add(p.getName());
					}
				}
				returnString.add("<empty>");
			}
		}
		return returnString;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (label.equalsIgnoreCase("createblock")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("You need to be a player!");
				return false;
			}
			Player player = (Player) sender;
			if (player.isOp()) {
				String select, select2;
				if (args.length == 0) {
					select =f "<AVAILABLE>";
				} else {
					if (args[0].equalsIgnoreCase("<empty>")) {
						select = "<AVAILABLE>";
					} else {
						Player create = Bukkit.getPlayer(args[0]);
						if (create == null) {
							player.sendMessage(Utils.announceToChat(
									"Unknown player! Make sure they are online or their name was spelled correctly! If you want to create an empty cell, either put '<empty>' or leave it blank."));
							return false;
						}
						select = create.getName();
					}
				}
				select2 = (args.length > 1) ? args[0] : allPlots.size() + "";
				int x = player.getLocation().getBlockX(), y = player.getLocation().getBlockY(),
						z = player.getLocation().getBlockZ();
				String index = "plot-" + select2; // plot-<ID>
				ConfigManager.getMainConfiguration().set("custom.BuildingGames." + index, select);
				ConfigManager.saveMain();
				ProtectedRegion newPlot = new ProtectedCuboidRegion(index, BlockVector3.at(x, y - 1, z),
						BlockVector3.at(x + 25, y + 23, z + 27));

				if (!isAvailable(select)) {
					newPlot.getMembers().addPlayer(select);
				}

				newPlot.setPriority(10);
				try {
					newPlot.setParent(regions.getRegion("building_games"));
				} catch (CircularInheritanceException e) {
					e.printStackTrace();
				}
				newPlot.setFlag(Flags.BUILD, StateFlag.State.ALLOW);
				newPlot.setFlag(Flags.BUILD.getRegionGroupFlag(), RegionGroup.MEMBERS);
				newPlot.setFlag(Flags.USE, StateFlag.State.ALLOW);
				newPlot.setFlag(Flags.USE.getRegionGroupFlag(), RegionGroup.MEMBERS);
				allPlots.put(index, select);
				BuildingGames.regions.addRegion(newPlot);
				player.sendMessage(Utils.announceToChat("Created build plot with ID " + ChatColor.GOLD + select2));
				return true;
			} else {
				player.sendMessage(Utils.announceToChat("You don't have permissions for this command!"));
				return true;
			}
		} else if (label.equalsIgnoreCase("generateplots")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				final UUID puuid = player.getUniqueId();
				final double ox = player.getLocation().getBlockX();
				final double oz = player.getLocation().getBlockZ();
				if (player.isOp()) {
					int ctr = 0;
					for (int x = 0; x < 320; x += 40) {
						for (int z = 0; z < 330; z += 42) { // x offset = 41; z offset = 43
							final double fx = x;
							final double fz = z;
							Bukkit.getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {

								@Override
								public void run() {
									Player p = Bukkit.getPlayer(puuid);
									Location pl = new Location(p.getWorld(), ox + fx,
											(double) p.getLocation().getBlockY(), oz + fz);
									p.teleport(pl);
									p.performCommand("createblock");
								}
							}, 5 * ctr++);
						}
					}

				} else {
					player.sendMessage(Utils.announceToChat("You don't have permissions for this command!"));
					return true;
				}
			}

		} else if (label.equalsIgnoreCase("joinbg")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				if (player.isOp()) {
					for (int x = 0; x < allPlots.size(); x++) {
						if (isUsable("plot-" + x)) {
							ProtectedRegion plot = regions.getRegion("plot-" + x);
							if (plot == null) {
								continue;
							}
							if (!plot.hasMembersOrOwners()) {
								// Select
								plot.getMembers().addPlayer(player.getName());
								BlockVector3 result = (plot.getMaximumPoint().add(plot.getMinimumPoint())).divide(2);
								player.teleport(new Location(player.getWorld(), result.getBlockX(), result.getBlockY(),
										result.getBlockZ()));
								return true;
							} else
								continue;
						} else {
							player.sendMessage(Utils.announceToChat(ChatColor.RED + "Sorry, the game is full!"));
						}
					}
				} else {
					player.sendMessage(Utils.announceToChat("You don't have permissions for this command!"));
					return true;
				}
			}
		} else if (label.equalsIgnoreCase("bg")) {
			if (args.length == 0) {
				return false;
			}
			if (args[0].equalsIgnoreCase("start")) {
				if (taskId == -1) {
					// taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(mainPlugin, this,
					// 20L, 20L);
				}
				regions.getRegion("building_games").setPriority(10);
				timer = Integer.parseInt(args[1]);
				started = true;
			} else if (args[0].equalsIgnoreCase("pause")) {
				started = false;
				regions.getRegion("building_games").setPriority(20);
			} else if (args[0].equalsIgnoreCase("set")) {
				timer = Integer.parseInt(args[1]);
			} else if (args[0].equalsIgnoreCase("fly")) {
				if (sender instanceof Player) {
					Player p = (Player) sender;
					p.getInventory().addItem(flapItem);
					ItemStack noDamage = new ItemStack(Material.IRON_SWORD);
					ItemMeta ndMeta = noDamage.getItemMeta();
					ndMeta.setDisplayName(ChatColor.GOLD + "Skill: Ninja");
					noDamage.setItemMeta(ndMeta);
					p.getInventory().addItem(noDamage);
				}
			}

			else if (args[0].equalsIgnoreCase("stop")) {
				Bukkit.getScheduler().cancelTask(taskId);
				started = false;
				taskId = -1;
				for (Player p : Bukkit.getOnlinePlayers()) {
					if (p.getGameMode() == GameMode.CREATIVE) {
						Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),
								"warp building_end " + p.getName());
					}
				}
			}
		} else if (label.equalsIgnoreCase("enablewings")) {
			if (!sender.isOp()) {
				return true;
			}
			if (args.length != 0) {
				if (args[0].equalsIgnoreCase("false")) {
					startedHG = false;
					Bukkit.getScheduler().cancelTask(taskId);
					sender.sendMessage(ChatColor.RED + "Wings are disabled!");
					return true;
				}
			}
			if (startedHG) {
				sender.sendMessage(ChatColor.GREEN + "Already enabled!");
				return true;
			}
			startedHG = true;
			lastHit = new HashMap<UUID, Long>();
			Bukkit.getServer().broadcastMessage(ChatColor.BOLD + "" + ChatColor.YELLOW + "Wings have been enabled!");
			Bukkit.getServer().broadcastMessage(
					ChatColor.GOLD + "Right click " + ChatColor.AQUA + "while holding the feather to jump!");
			Bukkit.getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {

				@Override
				public void run() {
					taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(mainPlugin, this, 200L, 200L);
					Bukkit.getServer().broadcastMessage(ChatColor.RED
							+ "Bloodlust enabled! Players would need to hit a player every 10 seconds otherwise health will start to drain!");
				}

			}, 20L * 120L);
			return true;
		}
		return false;
	}

	public static void initialize() {
		if (ConfigManager.getMainConfiguration().isConfigurationSection("custom.BuildingGames")) {
			for (String str : ConfigManager.getMainConfiguration().getConfigurationSection("custom.BuildingGames") // STR
																													// =
																													// plot-<ID>
					.getKeys(false)) {
				ProtectedRegion rg = BuildingGames.regions.getRegion(str);
				if (rg == null) {
					ConfigManager.getMainConfiguration().set("custom.BuildingGames." + str, null);
				} else {
					allPlots.put(rg.getId(),
							ConfigManager.getMainConfiguration().getString("custom.BuildingGames." + str));
				}
			}
		} else {
			ConfigManager.getMainConfiguration().createSection("custom.BuildingGames");
		}
	}

	public static void save() {
		for (String index : allPlots.keySet()) {
			ProtectedRegion rg = BuildingGames.regions.getRegion(index);
			DefaultDomain members = rg.getMembers();
			if (members.size() != 0) {
				ConfigManager.getMainConfiguration().set("custom.BuildingGames." + rg.getId(),
						members.getPlayers().iterator().next());
			}
			//
		}
	}

	public boolean isUsable(String plotIndex) {
		return allPlots.get(plotIndex) == "<AVAILABLE>";
	}

	public boolean isAvailable(String test) {
		return test == "<AVAILABLE>";
	}

	public String getPlotIndex(String index) {
		return "custom.BuildingGames." + index;
	}

	@Override
	public void run() {

		if (startedHG) {
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.getGameMode() == GameMode.SPECTATOR || p.getGameMode() == GameMode.CREATIVE) {
					continue;
				}
				if (lastHit.containsKey(p.getUniqueId())) {
					if (Math.abs(System.currentTimeMillis() - lastHit.get(p.getUniqueId())) >= 50000) {
						p.damage(2d);
						p.sendMessage(ChatColor.RED + "Your health is decaying! Please fight a player!");
						p.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 30 * 20, 1, true));
					}
				} else {
					if (p.getGameMode() == GameMode.SURVIVAL || p.getGameMode() == GameMode.ADVENTURE) {
						lastHit.put(p.getUniqueId(), 0L);
					}
				}
			}
		}
//		if (started) {
//			timer--;
//			for (Player p : Bukkit.getOnlinePlayers()) {
//				int minutes = timer / 60;
//				int seconds = timer % 60;
//				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
//						ChatColor.GREEN + "Time Remaining: " + ChatColor.YELLOW + minutes + ":" + seconds));
//				if (timer <= 0) {
//					p.sendTitle(ChatColor.DARK_RED + "TIMES UP!", ChatColor.YELLOW + "You're no longer able to build.",
//							10, 70, 20);
//					stopAll();
//					if (timer <= -5) {
//						Bukkit.getScheduler().cancelTask(taskId);
//					}
//				}
//			}
//
//		} else {
//			for (Player p : Bukkit.getOnlinePlayers()) {
//				int minutes = timer / 60;
//				int seconds = timer % 60;
//				p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
//						ChatColor.RED + "PAUSED! Time remaining: " + ChatColor.YELLOW + minutes + ":" + seconds));
//			}
//		}

	}

	public void stopAll() {
		ProtectedRegion buildingGames = regions.getRegion("building_games");
		buildingGames.setPriority(20);
	}

	@EventHandler
	public void checkLastHit(EntityDamageByEntityEvent e) {
		if (e.getDamager().equals(e.getEntity())) {
			return;
		}
		if (!(e.getDamager() instanceof Player || e.getEntity() instanceof Player)) {
			return;
		}
		if (startedHG) {
			lastHit.put(((Player) e.getDamager()).getUniqueId(), System.currentTimeMillis());
		}
	}

	@EventHandler
	public void onDoubleJump(PlayerInteractEvent e) {
		if (!startedHG) {
			return;
		}
		Player caster = e.getPlayer();
		if (!(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK
				|| e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK)) {
			return;
		}
		if (caster.getInventory() == null)
			return;
		if (caster.getInventory().getItemInMainHand() == null) {
			return;
		}
		ItemStack main = caster.getInventory().getItemInMainHand();
//		if (main.getType() == Material.IRON_SWORD) {
//			if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK) {
//				Block target = e.getPlayer().getTargetBlock(null, 10);
//				LivingEntity nearest = null;
//				double d2 = Integer.MAX_VALUE;
//				for (Entity ent : target.getWorld().getNearbyEntities(target.getLocation(), 5.0f, 5.0f, 5.0f)) {
//					if (ent instanceof LivingEntity) {
//						if (ent.equals(e.getPlayer())) {
//							continue;
//						}
//						if (nearest == null) {
//							nearest = (LivingEntity) ent;
//						} else {
//							if (ent.getLocation().distanceSquared(target.getLocation()) < d2) {
//								nearest = (LivingEntity) ent;
//							}
//						}
//					}
//				}
//				if (nearest != null) {
//					Vector location = VectorUtils.parabolicVelocity(VectorUtils.toVector(e.getPlayer().getLocation()),
//							VectorUtils.toVector(nearest.getLocation()), 2);
//					Arrow arrow = e.getPlayer().getWorld().spawnArrow(e.getPlayer().getEyeLocation(), location, 1.4f,
//							0f);
//					arrow.getLocation().getDirection().multiply(1d);
//					e.getPlayer().teleport(nearest.getLocation()
//							.subtract(nearest.getEyeLocation().getDirection().multiply(new Vector(1.5d, 0, 1.5d))));
//				}
//
//			}
//		}

		if (caster.getInventory().getItemInMainHand() != null) {
			if (!canFlap.containsKey(e.getPlayer().getUniqueId())) {
				canFlap.put(e.getPlayer().getUniqueId(), 0L);
			}
			if (main.getType() == Material.FEATHER
					&& main.getItemMeta().getLore().equals(flapItem.getItemMeta().getLore())) {

				if (canFlap.get(caster.getUniqueId()) == 0L) {
					caster.playSound(caster.getLocation(), Sound.ENTITY_BAT_TAKEOFF, 5, 0.8f);
					caster.setFallDistance(0.0f);
					Vector direction = caster.getLocation().getDirection();
					direction.multiply(0.25f);
					direction.setY(1.2);

					caster.setVelocity(direction);
					caster.getWorld().spawnParticle(Particle.CLOUD, caster.getLocation(), 100, 0, 0, 0, 0.05);
					canFlap.put(caster.getUniqueId(), System.currentTimeMillis() + 4 * 1000);
					final UUID casterUUID = caster.getUniqueId();
					Bukkit.getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {

						@Override
						public void run() {
							canFlap.put(casterUUID, 0L);
							Player p = Bukkit.getServer().getPlayer(casterUUID);
							p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_EGG, 1, 1);
							p.sendActionBar(ChatColor.GREEN + "You may now flap again!");
						}
					}, 20 * 4);
					e.setCancelled(true);
				} else {
					double round = Math.round(
							((canFlap.get(caster.getUniqueId()) - (double) System.currentTimeMillis()) / 10)) / 100.0;
					caster.sendActionBar(ChatColor.RED + "You must wait " + ChatColor.YELLOW + round + ChatColor.RED
							+ " before flapping.");
					e.setCancelled(true);
				}

			}
		} // end flap
	}

	// @EventHandler(priority = EventPriority.HIGHEST)
	public void onDamage(EntityDamageByEntityEvent e) {
		if (!(e.getEntity() instanceof LivingEntity)) {
			return;
		}
		if (e.getDamager() instanceof Player) {
			Player damager = (Player) e.getDamager();
			final LivingEntity damaged = (LivingEntity) e.getEntity();
			damaged.getWorld().spawnParticle(Particle.CRIT, damaged.getEyeLocation(), 100, 0.5, 0.5, 0.5, 0.05);
			Bukkit.getScheduler().scheduleSyncDelayedTask(mainPlugin, new Runnable() {
				@Override
				public void run() {
					damaged.setNoDamageTicks(100);
				}
			}, 1L);
			if (damager.getInventory() == null)
				return;
			if (damager.getInventory().getItemInMainHand() == null)
				return;
			// ItemStack item = damager.getInventory().getItemInMainHand();
			// if (item.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.GOLD +
			// "Skill: Ninja")) {
			// ((LivingEntity) e.getEntity()).setNoDamageTicks(0);
			// }
		}
	}
*/
}
