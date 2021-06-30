package github.kairusds.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import github.kairusds.Main;
import github.kairusds.task.HtopTask;
import java.util.ArrayList;
import java.util.UUID;

public class HtopManager{

	private Main plugin;

	private HtopTask task = null;
	private ArrayList<UUID> users = new ArrayList<>();

	public HtopManager(Main main){
		plugin = main;
	}

	Server getServer(){
		return plugin.getServer();
	}

	public boolean isTaskActive(){
		return task != null;
	}

	public void startTask(){
		task = new HtopTask(plugin);
		getServer().getScheduler().scheduleRepeatingTask(this, task, 20);
	}

	public void stopTask(){
		task.cancel();
		task = null;
	}

	public boolean isUser(Player player){
		return users.contains(player.getUniqueId());
	}

	public void addUser(Player player){
		users.add(player.getUniqueId());
	}

	public void removeUser(Player player){
		users.remove(users.indexOf(player.getUniqueId()));
	}
}