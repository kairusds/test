package github.kairusds;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.EntityDamageEvent;
import static cn.nukkit.event.entity.EntityDamageEvent.DamageCause.*; // why am i doing this
import cn.nukkit.event.player.PlayerInteractEvent;
import static cn.nukkit.event.player.PlayerInteractEvent.Action.*;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.LoginChainData;
import github.kairusds.manager.*;

public class EventListener implements Listener{

	private Main plugin;

	private final String NO_FALL_NAME = "boosted";

	public EventListener(Main main){
		plugin = main;
	}

	Server getServer(){
		return plugin.getServer();
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event){
		Entity entity = event.getEntity();
		if(entity instanceof Player){
			if(event.getCause() == FALL && entity.namedTag.contains(NO_FALL_NAME)){ // i used nbt bc i dont wanna define an arraylist again
				event.setCancelled();
				entity.namedTag.remove(NO_FALL_NAME);
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
		if(event.getAction() == RIGHT_CLICK_AIR && player.getInventory().getItemInHand().getId == 280){
			if(!player.namedTag.contains(NO_FALL_NAME)) player.namedTag.putByte(NO_FALL_NAME, 1);
			player.setMotion(event.getTouchVector());
			// add sound and particles
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event){
		HtopManager manager = plugin.getHtopManager();
		if(manager.isTaskActive() && getServer().getOnlinePlayers().size() < 1){
			manager.stopTask();
			getServer().getLogger().info("Disabled htop task.");
		}
	}
}