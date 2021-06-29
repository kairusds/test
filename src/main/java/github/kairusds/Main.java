package github.kairusds;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import github.kairusds.command.*;
import github.kairusds.network.protocol.*;
import github.kairusds.task.HtopTask;
import java.util.ArrayList;
import java.util.UUID;

public class Main extends PluginBase implements Listener{

	private HtopTask htopTask = null;
	private ArrayList<UUID> htopUsers = new ArrayList<UUID>();

	@Override
	public void onLoad(){
		getLogger().info(TextFormat.WHITE + "I've been loaded!");
	}

	@Override
	public void onEnable(){
		getLogger().info(TextFormat.DARK_GREEN + "I've been enabled!");
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		registerCommands();
		registerPackets();
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

	public boolean isHtopTaskRunning(){
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
		return htopUsers.contains((UUID) player.getUniqueId());
	}

	public void addHtopUser(Player player){
		htopUsers.add((UUID) player.getUniqueId());
	}

	public void removeHtopUser(Player player){
		htopUsers.remove(htopUsers.indexOf((UUID) player.getUniqueId()));
	}

	@Override
	public void onDisable(){
		getLogger().info(TextFormat.DARK_RED + "I've been disabled!");
	}
}