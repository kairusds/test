package github.kairusds.manager;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import github.kairusds.Main;
import github.kairusds.task.EntityTrackingTask;
import java.util.HashMap;
import java.util.UUID;

public class EntityTrackingManager{
	private Main plugin;

	private EntityTrackingTask task = null;
	private HashMap<UUID, Entity> users = new HashMap<>();

	public EntityTrackingManager(Main main){
		plugin = main;
	}

	public boolean isTaskActive(){
		return task != null;
	}

	public void startTask(){
		task = new EntityTrackingTask(plugin);
		plugin.getServer().getScheduler().scheduleRepeatingTask(plugin, task, 20);
	}

	public void stopTask(){
		task.cancel();
		task = null;
	}

	public boolean isUser(Player player){
		return users.containsKey(player.getUniqueId());
	}

	public void addUser(Player player, Entity block){
		users.put(player.getUniqueId(), block);
	}

	public void removeUser(Player player){
		users.remove(player.getUniqueId());
		SetSpawnPositionPacket pk = new SetSpawnPositionPacket();
		pk.spawnType = SetSpawnPositionPacket.TYPE_WORLD_SPAWN;
		Position spawn = player.getLevel().getSpawnLocation();
		pk.x = spawn.getFloorX();
		pk.y = spawn.getFloorY();
		pk.z = spawn.getFloorZ();
		player.dataPacket(pk);
	}

	public Entity getEntity(Player player){
		return users.get(player.getUniqueId());
	}

}