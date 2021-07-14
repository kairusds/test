package github.kairusds.task;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.utils.DyeColor;
import github.kairusds.Main;

public class RainbowArmorTask extends PluginTask<Main>{

	private Player player;
	private int colorIndex = 0;

	public BlockTrackingTask(Main owner){
		super(owner);
	}

	public void onRun(int currentTick){
		colorIndex++;
		if(colorIndex > 15){ // exceeds length of dye colors
			colorIndex = 0;
		}

		for(Player player : getOwner().getServer().getOnlinePlayers().values()){
			PlayerInventory inventory = player.getInventory();

			if(inventory.getHelmet().getId() == Item.LEATHER_CAP){
				((ItemColorArmor) inventory.getHelmet()).setColor(DyeColor.getByDyeData(colorIndex));
			}

			if(inventory.getHelmet().getId() == Item.LEATHER_TUNIC){
				((ItemColorArmor) inventory.getChestplate()).setColor(DyeColor.getByDyeData(colorIndex));
			}

			if(inventory.getHelmet().getId() == Item.LEATHER_PANTS){
				((ItemColorArmor) inventory.getLeggings()).setColor(DyeColor.getByDyeData(colorIndex));
			}

			if(inventory.getHelmet().getId() == Item.LEATHER_BOOTS){
				((ItemColorArmor) inventory.getBoots()).setColor(DyeColor.getByDyeData(colorIndex));
			}
		}
	}
}