package github.kairusds;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.utils.LoginChainData;
import github.kairusds.command.ImageMapCommand;

public class Main extends PluginBase implements Listener{

	@Override
	public void onLoad(){
		getLogger().info(TextFormat.WHITE + "I've been loaded!");
	}

	@Override
	public void onEnable(){
		getLogger().info(TextFormat.DARK_GREEN + "I've been enabled!");
		getLogger().info(String.valueOf(this.getDataFolder().mkdirs()));
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		getServer().getCommandMap().register("Test", new ImageMapCommand(this));
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onLogin(PlayerLoginEvent event){
		LoginChainData loginData = event.getPlayer().getLoginChainData();
		getServer().broadcastMessage("Device Model:" + loginData.getDeviceModel() + "\nDevice ID:" + loginData.getDeviceId() + "\nDevice OS:" + loginData.getDeviceOS());
	}

	@Override
	public void onDisable(){
		getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
	}
}