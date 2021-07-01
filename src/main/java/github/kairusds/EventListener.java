package github.kairusds;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import static cn.nukkit.event.entity.EntityDamageEvent.DamageCause.*; // why am i doing this
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import static cn.nukkit.event.player.PlayerInteractEvent.Action.*;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.form.response.FormResponse;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.LoginChainData;
import cn.nukkit.item.ItemMap;
import cn.nukkit.inventory.Inventory;
import github.kairusds.manager.*;
import java.awt.image.BufferedImage;

public class EventListener implements Listener{

	private Main plugin;

	public EventListener(Main main){
		plugin = main;
	}

	protected Server getServer(){
		return plugin.getServer();
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		if(entity instanceof Player){
			if(event.getCause() == FALL && entity.namedTag.contains("boosted")){ // i used nbt bc i dont wanna define an arraylist again
				event.setCancelled();
				entity.namedTag.remove("boosted");
				entity.getLevel().addSound(entity, Sound.MOB_BLAZE_HIT);
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
			FormResponseCustom res = (FormResponseCustom) response;

			if(mapManager.isUser(player)){
				if(response == null){
					mapManager.removeUser(player);
					return;
				}

				String url = res.getInputResponse(0);
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

				Inventory inventory = player.getInventory();
				if(inventory.canAddItem(map)){
					inventory.addItem(map);
				}else{
					player.sendMessage("§7Inventory full, dropping item instead.");
					player.getLevel().dropItem(player, map);
				}
				mapManager.removeUser(player);
			}

			if(settingsManager.isUser(player)){
				StringBuilder changes = new StringBuilder();
				String displayName = res.getInputResponse(0);
				String nameTag = res.getInputResponse(1);
				int gamemode = Server.getGamemodeFromString(res.getDropdownResponse(2).getElementContent()); // cool chain
				boolean invisible = res.getToggleResponse(3);

				if(player.getDisplayName() != displayName){
					player.setDisplayName(displayName);
					changes.append("§edisplay name §7-> §b" + displayName);
				}

				if(player.getNameTag() != nameTag){
					player.setDisplayName(nameTag);
					changes.append("§enametag §7-> §b" + nameTag);
				}

				if(player.getGamemode() != gamemode){
					player.setGamemode(gamemode);
					changes.append("§egamemode §7-> §b" + Server.getGamemodeString(gamemode));
				}

				if(player.getDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_INVISIBLE) != invisible){
					entity.setDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_INVISIBLE, invisible);
					entity.setNameTagVisible(invisible ? false : true);
					changes.append("§edisplay name §7-> §b" + displayName);
				}

				if(player.isSurvival() || player.isAdventure()){
					boolean hunger = res.getToggleResponse(4);
					boolean flight = res.getToggleResponse(5);
					if(player.isFoodEnabled() != hunger){
						player.setFoodEnabled(hunger);
						changes.append("§ehunger §7-> §b" + (hunger ? "on" : "off"));
					}
					if(player.getAllowFlight() != flight){
						player.setAllowFlight(flight);
						changes.append("§eflight §7-> §b" + (flight ? "on" : "off"));
					}
				}
				player.sendMessages("§7Settings saved. Changes: " + String.join("§8, ", changes));
			}
		}
	}

	@EventHandler
	public void onLogin(PlayerLoginEvent event){
		LoginChainData loginData = event.getPlayer().getLoginChainData();
		String msg = "Device Model:" + loginData.getDeviceModel() + "\nDevice ID:" + loginData.getDeviceId() + "\nDevice OS:" + loginData.getDeviceOS();
		getServer().getLogger().info(msg);

		for(Player player : getServer().getOnlinePlayers().values()){
			if(player.hasPermission("kairusds.message.device")){
				player.sendMessage(msg);
			}
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event){
		HtopManager manager = plugin.getHtopManager();
		if(!manager.isTaskActive()){
			manager.startTask();
			getServer().getLogger().info("Enabled htop task.");
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(event.getAction() == RIGHT_CLICK_AIR && player.getInventory().getItemInHand().getId() == 280){
			event.setCancelled();
			if(!player.namedTag.contains("boosted") && (player.isSurvival() || player.isAdventure())){
				player.namedTag.putByte("boosted", 1);
				player.setCheckMovement(false);
			}
			player.setMotion(event.getTouchVector().multiply(2.7).up(2));
			player.getLevel().addSound(player, Sound.MOB_ENDERDRAGON_FLAP);
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		HtopManager manager = plugin.getHtopManager();
		if(manager.isTaskActive() && getServer().getOnlinePlayers().size() <= 1){
			manager.stopTask();
			getServer().getLogger().info("Disabled htop task.");
		}
	}
}