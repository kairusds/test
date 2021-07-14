package github.kairusds.task;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.scheduler.PluginTask;
import cn.nukkit.utils.DyeColor;
import github.kairusds.Main;

public class RainbowArmorTask extends PluginTask<Main>{

	private Player player;
	private int colorIndex = 0;

	public RainbowArmorTask(Main owner){
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
				player.getInventory().setHelmet(((ItemColorArmor) inventory.getHelmet()).setColor(DyeColor.getByDyeData(colorIndex)));
			}

			if(inventory.getChestplate().getId() == Item.LEATHER_TUNIC){
				player.getInventory().setChestplate(((ItemColorArmor) inventory.getChestplate()).setColor(DyeColor.getByDyeData(colorIndex)));
			}

			if(inventory.getLeggings().getId() == Item.LEATHER_PANTS){
				player.getInventory().setLeggings(((ItemColorArmor) inventory.getLeggings()).setColor(DyeColor.getByDyeData(colorIndex)));
			}

			if(inventory.getBoots().getId() == Item.LEATHER_BOOTS){
				player.getInventory().setBoots(((ItemColorArmor) inventory.getBoots()).setColor(DyeColor.getByDyeData(colorIndex)));
			}
		}
	}
}