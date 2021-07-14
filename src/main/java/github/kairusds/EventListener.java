package github.kairusds;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.entity.Entity;
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
import cn.nukkit.level.Sound;
import cn.nukkit.level.particle.DustParticle;
import cn.nukkit.math.Vector3;
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
			Entity damager = event.getDamager();
			Item heldItem = damager.getInventory().getItemInHand();

			if(damager instanceof Player && heldItem.getId() == Item.COMPASS){
				event.setCancelled();
				EntityTrackingManager manager = plugin.getEntityTrackingManager();
				if(!manager.isUser(player)){
					player.sendMessage("§7Started tracking distance of §e" + entity.getName());
					manager.addUser(player, entity);
				}else{
					player.sendMessage("§7Stopped tracking §e" + manager.getEntity(player).getName());
					manager.removeUser(player);
				}
			}
		}

		if(entity instanceof Player){
			if(event.getCause() == FALL && entity.namedTag.contains("boosted")){ // i used nbt bc i dont wanna define an arraylist again
				event.setCancelled();
				entity.namedTag.remove("boosted");
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
	public void onLogin(PlayerLoginEvent event){
		LoginChainData loginData = event.getPlayer().getLoginChainData();
		String msg = "Xbox User ID: " + loginData.getXUID() + "\nDevice Model: " + loginData.getDeviceModel() + "\nDevice ID: " + loginData.getDeviceId() + "\nDevice OS: " + loginData.getDeviceOS();
		getServer().getLogger().info(msg);

		for(Player player : getServer().getOnlinePlayers().values()){
			if(player.hasPermission("kairusds.message.device")){
				player.sendMessage(msg);
			}
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		PlayerInventory inventory = player.getInventory();
		Item heldItem = inventory.getItemInHand();
		Block block = event.getBlock();

		if(event.getAction() == RIGHT_CLICK_AIR && heldItem.getId() == Item.STICK){
			event.setCancelled();
			if(!player.namedTag.contains("boosted") && (player.isSurvival() || player.isAdventure())){
				player.namedTag.putByte("boosted", 1);
				player.setCheckMovement(false);
			}
			player.setMotion(event.getTouchVector().multiply(2.7).up());
			player.getLevel().addSound(player, Sound.MOB_ENDERDRAGON_FLAP, 0.6f, 1.0f);
		}

		if(heldItem.getId() == Item.COMPASS && block.isSolid()){
			event.setCancelled();
			BlockTrackingManager manager = plugin.getBlockTrackingManager();
			if(!manager.isUser(player)){
				player.sendMessage("§7Started tracking distance of §e" + block.getName());
				manager.addUser(player, block);
			}else{
				player.sendMessage("§7Stopped tracking §e" + manager.getBlock(player).getName());
				manager.removeUser(player);
			}
		}

		if(heldItem.getId() == Item.BLAZE_ROD){
			player.getLevel().addSound(player, Sound.TILE_PISTON_IN, 0.6f, 1.0f);
			new NukkitRunnable(){
				Location location = (Location) player; // might need to implement eyeheight if needed
				Vector3 direction = player.getDirectionVector();
				double time = 0;
				double rotation = 0;

				@Override
				public void run(){
					time += 1;
					double xtrav = direction.getX() * time;
					double ytrav = direction.getY() * time;
					double ztrav = direction.getZ() * time;
					location.add(xtrav, ytrav, ztrav);
			
					for(double i = 0; i <= 2 * Math.PI; i += Math.PI / 32){
						double x = rotation * Math.cos(i);
						double y = rotation * Math.cos(i) + 1.5;
						double z = rotation * Math.sin(i);
						location.add(x, y, z);
						player.getLevel().addParticle(new DustParticle((Vector3) location, 48, 48, 48));
						player.getLevel().addSound((Vector3) location, Sound.TILE_PISTON_OUT, 0.4f, 1.0f);
						location.subtract(x, y, z);
					}
					location.subtract(xtrav, ytrav, ztrav);
					rotation += 0.1;
					if(time > 20){
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
			inventory.setItemInHand(bow);
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		HtopManager manager = plugin.getHtopManager();
		BlockTrackingManager manager1 = plugin.getBlockTrackingManager();
		EntityTrackingManager manager2 = plugin.getBlockTrackingManager();
		if(!manager.isTaskActive() && !manager2.isTaskActive() && !manager2.isTaskActive()){
			manager.startTask();
			manager1.startTask();
			manager2.startTask();
			rainbowArmorTask = new RainbowArmorTask(plugin);
			plugin.getServer().getScheduler().scheduleRepeatingTask(plugin, rainbowArmorTask, 20);
			getServer().getLogger().info("Enabled tasks.");
		}
	}

	@EventHandler
	public void onJump(PlayerJumpEvent event){
		Player player = event.getPlayer();
		if(!player.namedTag.contains("jumped")) player.namedTag.putByte("jumped", 1);
		new NukkitRunnable(){
			@Override
			public void run(){
				player.namedTag.remove("jumped");
			}
		}.runTaskLater(plugin, 11);

		if(player.namedTag.contains("jumped")){
			if(!player.namedTag.contains("boosted") && (player.isSurvival() || player.isAdventure())){
				player.setCheckMovement(false);
				player.namedTag.putByte("boosted", 1);
			}

			player.setMotion(player.getDirectionVector().multiply(1.1).up());
			player.getLevel().addSound(player, Sound.ELYTRA_LOOP, 0.6f, 1.0f);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		HtopManager manager = plugin.getHtopManager();
		BlockTrackingManager manager1 = plugin.getBlockTrackingManager();
		EntityTrackingManager manager2 = plugin.getBlockTrackingManager();
		if(manager.isTaskActive() && manager1.isTaskActive() && manager2.isTaskActive() && getServer().getOnlinePlayers().size() <= 1){
			manager.stopTask();
			manager1.stopTask();
			manager2.stopTask();
			rainbowArmorTask.stop();
			rainbowArmorTask = null;
			getServer().getLogger().info("Disabled tasks.");
		}
	}
}