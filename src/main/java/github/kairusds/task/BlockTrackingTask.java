package github.kairusds.task;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.SetSpawnPositionPacket;
import cn.nukkit.scheduler.PluginTask;
import github.kairusds.Main;
import github.kairusds.manager.BlockTrackingManager;

public class BlockTrackingTask extends PluginTask<Main>{

	private Player player;
	private Block block;

	public BlockTrackingTask(Main owner){
		super(owner);
	}

	public void onRun(int currentTick){
		BlockTrackingManager manager = getOwner().getBlockTrackingManager();
		for(Player player : getOwner().getServer().getOnlinePlayers().values()){
			if(manager.isUser(player)){
				Block block = manager.getBlock(player);
				if(player.getLevel().getBlock(block).getId() == 0) manager.removeUser(player);
				Item heldItem = player.getInventory().getItemInHand();
				if(heldItem.getId() == Item.COMPASS){
					player.sendTip("§7Distance from §e" + block.getName() + "§8: §e" + Math.floor(player.distance(block)));

					SetSpawnPositionPacket pk = new SetSpawnPositionPacket();
					pk.spawnType = SetSpawnPositionPacket.TYPE_WORLD_SPAWN;
					pk.x = block.getFloorX();
					pk.y = block.getFloorY();
					pk.z = block.getFloorZ();
					player.dataPacket(pk);
				}
			}
		}
	}

}