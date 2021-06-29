package github.kairusds;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.utils.LoginChainData;

public class EventListener implements Listener{

	private Main plugin;

	public EventListener(Main plugin){
		plugin = plugin;
	}

	public Server getServer(){
		return plugin.getServer();
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onLogin(PlayerLoginEvent event){
		LoginChainData loginData = event.getPlayer().getLoginChainData();
		String info = "Device Model:" + loginData.getDeviceModel() + "\nDevice ID:" + loginData.getDeviceId() + "\nDevice OS:" + loginData.getDeviceOS();
		getServer().getConsoleSender().sendMessage(info);

		for(Player player : getServer().getOnlinePlayers().values()){
			if(player.hasPermission("test.message.device")){
				player.sendMessage(info);
			}
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onJoin(PlayerJoinEvent event){
		if(!plugin.isHtopTaskRunning()){
			plugin.startHtopTask();
		}
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onQuit(PlayerQuitEvent event){
		if(plugin.isHtopTaskRunning() && getServer().getOnlinePlayers().size() < 1){
			plugin.stopHtopTask();
		}
	}
}