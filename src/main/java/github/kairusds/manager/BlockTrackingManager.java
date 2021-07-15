package github.kairusds.manager;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import github.kairusds.Main;
import github.kairusds.task.BlockTrackingTask;
import java.util.HashMap;
import java.util.UUID;

public class BlockTrackingManager{

	private Main plugin;

	private BlockTrackingTask task = null;
	private HashMap<UUID, Block> users = new HashMap<>();

	public BlockTrackingManager(Main main){
		plugin = main;
	}

	public boolean isTaskActive(){
		return task != null;
	}

	public void startTask(){
		task = new BlockTrackingTask(plugin);
		plugin.getServer().getScheduler().scheduleRepeatingTask(plugin, task, 1);
	}

	public void stopTask(){
		task.cancel();
		task = null;
	}

	public boolean isUser(Player player){
		return users.containsKey(player.getUniqueId());
	}

	public void addUser(Player player, Block block){
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

	public Block getBlock(Player player){
		return users.get(player.getUniqueId());
	}

}