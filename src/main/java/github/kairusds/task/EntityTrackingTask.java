package github.kairusds.task;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.scheduler.PluginTask;
import github.kairusds.Main;
import github.kairusds.manager.EntityTrackingManager;

public class EntityTrackingTask extends PluginTask<Main>{

	private Player player;
	private Entity entity;

	public EntityTrackingTask(Main owner){
		super(owner);
	}

	public void onRun(int currentTick){
		EntityTrackingManager manager = getOwner().getEntityTrackingManager();
		for(Player player : getOwner().getServer().getOnlinePlayers().values()){
			if(manager.isUser(player)){
				Entity entity = manager.getEntity(player);
				Item heldItem = player.getInventory().getItemInHand();
				if(heldItem.getId() == Item.COMPASS){
					player.sendTip("§7Distance from §e" + entity.getName() + "§7:§e" + player.distanceSquared((Vector3) entity)));
					SetSpawnPositionPacket pk = new SetSpawnPositionPacket();
					pk.spawnType = SetSpawnPositionPacket.TYPE_WORLD_SPAWN;
					pk.x = entity.getFloorX();
					pk.y = entity.getFloorY();
					pk.z = entity.getFloorZ();
					player.dataPacket(pk);
				}
			}
		}
	}
}