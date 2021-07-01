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
import cn.nukkit.form.response.FormResponseSimple;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.window.FormWindow;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.LoginChainData;
import github.kairusds.manager.*;
import java.util.HashMap;

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
		Level level = entity.getLevel();
		if(entity instanceof Player){
			if(event.getCause() == FALL && entity.namedTag.contains("boosted")){ // i used nbt bc i dont wanna define an arraylist again
				event.setCancelled();
				((Player) entity).setAllowFlight(false);
				entity.namedTag.remove("boosted");
				entity.getLevel().addSound(entity, Sound.FALL_AMETHYST_BLOCK);
			}
		}
	}

	@EventHandler
	public void onFormRespond(PlayerFormRespondedEvent event){
		Player player = event.getPlayer();
		FormWindow window = event.getWindow();
		FormResponse response = event.getResponse();
		ImageMapManager manager = plugin.getImageMapManager();

		if(event.wasClosed()){
			if(manager.isUser(player)){
				manager.removeUser(player);
			}
			player.sendMessage("§7Form closed.");
		}

		if(window instanceof FormWindowCustom){
			if(manager.isUser(player)){
				if(response == null){
					manager.removeUser(player);
					return;
				}
				for(HashMap.Entry<Integer, Object> element : ((FormResponseCustom) response).getResponses().entrySet()){
					player.sendMessage(element.getKey() + " - " + element.getValue().toString());
				}
				manager.removeUser(player);
			}
		}

		if(window instanceof FormWindowSimple){
			FormResponseSimple simpleResponse = (FormResponseSimple) response;
			player.sendMessage(simpleResponse.getClickedButtonId() + " " + simpleResponse.getClickedButton().getText());
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
			if(!player.namedTag.contains("boosted")) player.namedTag.putByte("boosted", 1);
			player.setAllowFlight(true);
			player.setMotion(event.getTouchVector().multiply(2.5));
			player.getLevel().addSound(player, Sound.MOB_SHULKER_SHOOT);
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