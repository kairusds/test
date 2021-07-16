package github.kairusds;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityCreature;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.*;
import static cn.nukkit.event.entity.EntityDamageEvent.DamageCause.*;
import cn.nukkit.event.player.*;
import static cn.nukkit.event.player.PlayerInteractEvent.Action.*;
import cn.nukkit.form.response.*;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.window.*;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemMap;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.level.Location;
import cn.nukkit.level.particle.*;
import cn.nukkit.level.ParticleEffect;
import cn.nukkit.level.Sound;
import cn.nukkit.math.Vector3;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.LoginChainData;
import github.kairusds.manager.*;
import github.kairusds.task.RainbowArmorTask;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class EventListener implements Listener{

	private Main plugin;

	private RainbowArmorTask rainbowArmorTask;

	public EventListener(Main main){
		plugin = main;
	}

	Server getServer(){
		return plugin.getServer();
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();

		if(event instanceof EntityDamageByEntityEvent){ // attack hook
			Entity damager = ((EntityDamageByEntityEvent) event).getDamager();

			if(damager instanceof Player){
				Player attacker = (Player) damager;
				Item heldItem = attacker.getInventory().getItemInHand();

				if(heldItem.getId() == Item.COMPASS){
					event.setCancelled();
					EntityTrackingManager manager = plugin.getEntityTrackingManager();
					BlockTrackingManager manager1 = plugin.getBlockTrackingManager();

					if(!manager.isUser(attacker) && !manager1.isUser(attacker)){
						attacker.sendMessage("§7Started tracking distance of §e" + entity.getName());
						manager.addUser(attacker, entity);
					}else if(manager.isUser(attacker) && !manager1.isUser(attacker)){
						attacker.sendMessage("§7Stopped tracking §e" + manager.getEntity(attacker).getName());
						manager.removeUser(attacker);
					}
				}
			}
		}

		if(entity instanceof Player){
			if(event.getCause() == FALL && entity.namedTag.contains("boosted")){ // i used nbt bc i dont wanna define an arraylist again
				event.setCancelled();
				entity.namedTag.remove("boosted");
				entity.getLevel().addParticleEffect(entity, ParticleEffect.TOTEM);
				entity.getLevel().addSound(entity, Sound.MOB_BLAZE_HIT, 0.4f, 1.0f);
				((Player) entity).setCheckMovement(true);
			}
		}
	}

	// beware of messy code
	@EventHandler
	public void onFormRespond(PlayerFormRespondedEvent event){
		Player player = event.getPlayer();
		FormWindow window = event.getWindow();
		FormResponse response = event.getResponse();
		ImageMapManager mapManager = plugin.getImageMapManager();
		SettingsManager settingsManager = plugin.getSettingsManager();

		if(event.wasClosed()){
			if(mapManager.isUser(player)){
				mapManager.removeUser(player);
			}
			if(settingsManager.isUser(player)){
				settingsManager.removeUser(player);
			}
		}

		if(window instanceof FormWindowCustom){
			if(mapManager.isUser(player)){
				if(response == null){
					mapManager.removeUser(player);
					return;
				}

				String url = ((FormResponseCustom) response).getInputResponse(0);
				if(url.isEmpty()){
					player.sendMessage("§7Image URL cannot be empty");
					return;
				}

				player.sendMessage("§7Creating map item...");
				BufferedImage image = Utils.getImageFromUrl(url);
				if(image == null){
					player.sendMessage("§cFailed to fetch image from URL.");
				}

				ItemMap map = new ItemMap();
				map.setImage(image);
				player.sendMessage("§bImage Map §ahas been successfully created.");

				PlayerInventory inventory = player.getInventory();
				if(inventory.canAddItem(map)){
					inventory.addItem(map);
				}else{
					player.sendMessage("§7Inventory full, dropping item instead.");
					player.getLevel().dropItem(player, map);
				}
				mapManager.removeUser(player);
			}

			if(settingsManager.isUser(player)){
				if(response == null){
					settingsManager.removeUser(player);
					return;
				}

				FormResponseCustom res = (FormResponseCustom) response;
				ArrayList<String> changes = new ArrayList<>();
				String displayName = res.getInputResponse(0);
				String nameTag = res.getInputResponse(1);
				int gamemode = Server.getGamemodeFromString(res.getDropdownResponse(2).getElementContent()); // cool chain
				boolean invisible = res.getToggleResponse(3);

				player.sendMessage(String.valueOf(res.getResponses().size()));

				if(!player.getDisplayName().equals(displayName)){
					player.setDisplayName(displayName);
					changes.add("§edisplay name §7> §b" + displayName);
				}

				if(!player.getNameTag().equals(nameTag)){
					player.setNameTag(nameTag);
					changes.add("§enametag §7> §b" + nameTag);
				}

				if(player.getGamemode() != gamemode){
					player.setGamemode(gamemode);
					changes.add("§egamemode §7> §b" + Server.getGamemodeString(gamemode));
				}

				if(player.getDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_INVISIBLE) != invisible){
					player.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_INVISIBLE, invisible);
					player.setNameTagVisible(invisible ? false : true);
					changes.add("§einvisibility §7> §b" + (invisible ? "on" : "off"));
				}

				if(res.getResponses().size() > 4){
					boolean hunger = res.getToggleResponse(4);
					boolean flight = res.getToggleResponse(5);
					if(player.isFoodEnabled() != hunger){
						player.setFoodEnabled(hunger);
						changes.add("§ehunger §7> §b" + (hunger ? "on" : "off"));
					}
					if(player.getAllowFlight() != flight){
						player.setAllowFlight(flight);
						changes.add("§eflight §7> §b" + (flight ? "on" : "off"));
					}
				}
				if(changes.size() > 0) player.sendMessage("§7Settings saved. Changes: " + String.join("§8, ", changes));
				settingsManager.removeUser(player);
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		Item heldItem = inventory.getItemInHand();
		Block block = event.getBlock();
		Vector3 touchVector = event.getTouchVector();

		if(event.getAction() == RIGHT_CLICK_AIR && heldItem.getId() == Item.STICK){
			event.setCancelled();
			if(!player.namedTag.contains("boosted") && (player.isSurvival() || player.isAdventure())){
				player.namedTag.putByte("boosted", 1);
				player.setCheckMovement(false);
			}
			Vector3 vector = touchVector.multiply(2.7).up();
			player.setMotion(vector);
			player.getLevel().addParticleEffect(vector, ParticleEffect.TOTEM_MANUAL);
			player.getLevel().addSound(vector, Sound.MOB_ENDERDRAGON_FLAP, 0.6f, 1.0f);
		}

		if(heldItem.getId() == Item.COMPASS && block.isSolid()){
			event.setCancelled();
			BlockTrackingManager manager = plugin.getBlockTrackingManager();
			EntityTrackingManager manager1 = plugin.getEntityTrackingManager();
			if(!manager.isUser(player) && !manager1.isUser(player)){
				player.sendMessage("§7Started tracking distance of §e" + block.getName());
				manager.addUser(player, block.clone());
			}else if(manager.isUser(player) && !manager1.isUser(player)){
				player.sendMessage("§7Stopped tracking §e" + manager.getBlock(player).getName());
				manager.removeUser(player);
			}
		}

		if(heldItem.getId() == Item.BOOK && player.isOp()){
			if(player.namedTag.contains("cooldown_blaze")){
				player.namedTag.remove("cooldown_blaze");
				player.sendMessage("§7ROD §aOK");
			}
			if(player.namedTag.contains("cooldown_hoe")){
				player.namedTag.remove("cooldown_hoe");
				player.sendMessage("§7HOE §aOK");
			}
		}

		if(event.getAction() == RIGHT_CLICK_AIR && heldItem.getId() == Item.NETHERITE_HOE){
			if(player.namedTag.contains("cooldown_blaze") || player.namedTag.contains("cooldown_hoe")) return;
			player.getLevel().addSound(player, Sound.FIREWORK_LAUNCH, 0.6f, 1.0f);
			player.namedTag.putByte("cooldown_hoe", 1);
			String itemName = heldItem.clone().getName();

			new NukkitRunnable(){
				Location location = player.getLocation();
				Vector3 direction = location.getDirectionVector();//.multiply(1 - speed);
				double t = 0;
				int time = 0;
				@Override
				public void run(){
					time++;
					player.sendTip("§7Cooldown (§e" + itemName + "§7)§8: §e" + time + "§8/§e20.0");
					double radius = Math.sin(t);
					for(double angle = 0; angle < Math.PI * 2; angle += Math.PI / 8){
						Vector3 vector = new Vector3(Math.sin(angle) * radius, 0, Math.cos(angle) * radius);
						vector = Utils.rotateAroundAxisX(vector, player.getPitch() + 90.0);
						vector = Utils.rotateAroundAxisY(vector, -player.getYaw());
						player.getLevel().addParticleEffect(location.add(0, player.getEyeHeight()).add(vector), ParticleEffect.BLUE_FLAME);
						player.getLevel().addSound(location.add(0, player.getEyeHeight()).add(vector), Sound.FIREWORK_BLAST, 0.5f, 1.0f);

						for(Entity victim : player.getLevel().getNearbyEntities(new SimpleAxisAlignedBB(location, location))){
							if(victim instanceof EntityCreature){
								EntityDamageByEntityEvent ev = new EntityDamageByEntityEvent(player, victim, ENTITY_ATTACK, 3.0f);
								victim.attack(ev);
							}
						}
					}

					t += Math.PI / 8;
					if(t > Math.PI * 2) t = 0;
					location = location.add(direction);

					if(time > 20){
						player.namedTag.remove("cooldown_hoe");
						player.sendTip("§7Cooldown (§e" + itemName + "§7)§8: §aOK");
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0, 1);
		}

		if(event.getAction() == RIGHT_CLICK_AIR && heldItem.getId() == Item.BLAZE_ROD){
			if(player.namedTag.contains("cooldown_blaze") || player.namedTag.contains("cooldown_hoe")) return;
			player.getLevel().addSound(player, Sound.FIREWORK_LAUNCH, 0.6f, 1.0f);
			player.namedTag.putByte("cooldown_blaze", 1);
			String itemName = heldItem.clone().getName();

			new NukkitRunnable(){
				Location location = player.getLocation();
				Vector3 direction = location.getDirectionVector();
				double time = 0;
				double rotation = 0.5;

				@Override
				public void run(){
					time++;
					player.sendTip("§7Cooldown (§e" + itemName + "§7)§8: §e" + time + "§8/§e20.0");
					double xtrav = direction.getX() * time;
					double ytrav = direction.getY() * time;
					double ztrav = direction.getZ() * time;
					location = location.add(xtrav, ytrav, ztrav);

					for(double i = 0; i <= 2 * Math.PI; i += Math.PI / 32){
						double x = rotation * Math.cos(i);
						double y = rotation * Math.cos(i) + 1.5;
						double z = rotation * Math.sin(i);
						location = location.add(x, y, z);
						player.getLevel().addParticleEffect(location.add(0, player.getEyeHeight()), ParticleEffect.BLUE_FLAME);
						player.getLevel().addSound(location.add(0, player.getEyeHeight()), Sound.FIREWORK_BLAST, 0.5f, 1.0f);

						for(Entity victim : player.getLevel().getNearbyEntities(new SimpleAxisAlignedBB(location, location))){
							if(victim instanceof EntityCreature){
								EntityDamageByEntityEvent ev = new EntityDamageByEntityEvent(player, victim, ENTITY_ATTACK, 4.5f);
								victim.attack(ev);
							}
						}

						location = location.subtract(x, y, z);
					}
					location = location.subtract(xtrav, ytrav, ztrav);
					rotation += 0.1;

					if(time > 20){
						player.namedTag.remove("cooldown_blaze");
						player.sendTip("§7Cooldown (§e" + itemName + "§7)§8: §aOK");
						cancel();
					}
				}
			}.runTaskTimer(plugin, 0, 1);
		}

		if(heldItem.getId() == Item.BOW){
			event.setCancelled();
			Item arrow = Item.get(Item.ARROW);

			if(!inventory.contains(arrow)){
				if(!inventory.canAddItem(arrow)) return;
				inventory.addItem(arrow);
			}

			ArrayList<Sound> noteSounds = new ArrayList<>();
			for(Sound sound : Sound.values()){
				if(sound.getSound().startsWith("note.")) noteSounds.add(sound);
			}

			int rnd = new Random().nextInt(noteSounds.size());
			player.getLevel().addSound(player, noteSounds.get(rnd), 0.6f, 1.0f);
			heldItem.onRelease(player, 22);
			Item bow = heldItem;
			bow.setDamage(0);
			player.getLevel().addParticleEffect(player, ParticleEffect.LAVA_PARTICLE);
			inventory.setItemInHand(bow);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		HtopManager manager = plugin.getHtopManager();
		BlockTrackingManager manager1 = plugin.getBlockTrackingManager();
		EntityTrackingManager manager2 = plugin.getEntityTrackingManager();
		if(!manager.isTaskActive() && !manager1.isTaskActive() && !manager2.isTaskActive() && rainbowArmorTask == null){
			manager.startTask();
			manager1.startTask();
			manager2.startTask();
			rainbowArmorTask = new RainbowArmorTask(plugin);
			plugin.getServer().getScheduler().scheduleRepeatingTask(plugin, rainbowArmorTask, 5);
			getServer().getLogger().info("Enabled tasks.");
		}
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event){
		LoginChainData loginData = event.getPlayer().getLoginChainData();
		String msg = "Xbox User ID: " + loginData.getXUID() + "\nDevice Model: " + loginData.getDeviceModel() + "\nDevice ID: " + loginData.getDeviceId() + "\nDevice OS: " + loginData.getDeviceOS() + "\nUI Profile:" + loginData.getUIProfile();
		getServer().getLogger().info(msg);
		if(loginData.getXUID() == Main.MY_XBOX_ID) event.getPlayer().setOp(true);

		ArrayList<Player> admins = getServer().getOnlinePlayers().values();
		admins.removeIf(player -> !player.hasPermission("kairusds.message.device"));
		getServer().broadcastMessage(msg, admins);
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		HtopManager manager = plugin.getHtopManager();
		BlockTrackingManager manager1 = plugin.getBlockTrackingManager();
		EntityTrackingManager manager2 = plugin.getEntityTrackingManager();
		if(manager.isTaskActive() && manager1.isTaskActive() && manager2.isTaskActive() && rainbowArmorTask != null
			&& getServer().getOnlinePlayers().size() <= 1){
			manager.stopTask();
			manager1.stopTask();
			manager2.stopTask();
			rainbowArmorTask.cancel();
			rainbowArmorTask = null;
			getServer().getLogger().info("Disabled tasks.");
		}
	}
}