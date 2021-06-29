package github.kairusds;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import github.kairusds.command.*;
import github.kairusds.network.protocol.*;
import github.kairusds.task.HtopTask;
import java.util.ArrayList;
import java.util.UUID;

public class Main extends PluginBase{

	private HtopTask htopTask = null;
	private ArrayList<UUID> htopUsers = new ArrayList<>();
	private static Main instance;

	@Override
	public void onLoad(){
		getLogger().info(TextFormat.WHITE + "I've been loaded!");
	}

	@Override
	public void onEnable(){
		instance = this;
		getLogger().info(TextFormat.DARK_GREEN + "I've been enabled!");
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		registerCommands();
		registerPackets();
	}

	public static Main getInstance(){
		return instance;
	}

	private void registerCommands(){
		ArrayList<Command> commands = new ArrayList<>();
		commands.add(new ImageMapCommand(this));
		commands.add(new HtopCommand(this));
		getServer().getCommandMap().registerAll("test", commands);
	}

	// idk
	private void registerPackets(){
		getServer().getNetwork().registerPacket(RemoveObjectivePacket.NETWORK_ID, RemoveObjectivePacket.class);
		getServer().getNetwork().registerPacket(SetDisplayObjectivePacket.NETWORK_ID, SetDisplayObjectivePacket.class);
		getServer().getNetwork().registerPacket(SetScoreboardIdentityPacket.NETWORK_ID, SetScoreboardIdentityPacket.class);
		getServer().getNetwork().registerPacket(SetScorePacket.NETWORK_ID, SetScorePacket.class);
	}

	public boolean isHtopActive(){
		return htopTask != null;
	}

	public void startHtopTask(){
		htopTask = new HtopTask(this);
		getServer().getScheduler().scheduleRepeatingTask(this, htopTask, 20);
	}

	public void stopHtopTask(){
		htopTask.cancel();
		htopTask = null;
	}

	public boolean isHtopUser(Player player){
		if(htopUsers.isEmpty()) return false;
		return htopUsers.contains(player.getUniqueId());
	}

	public void addHtopUser(Player player){
		htopUsers.add(player.getUniqueId());
	}

	public void removeHtopUser(Player player){
		htopUsers.remove(htopUsers.indexOf(player.getUniqueId()));
	}

	@Override
	public void onDisable(){
		getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
	}
}