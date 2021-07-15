package github.kairusds.task;

import cn.nukkit.Player;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemColorArmor;
import cn.nukkit.scheduler.PluginTask;
import github.kairusds.Main;
import java.util.Random;

public class RainbowArmorTask extends PluginTask<Main>{

	private Player player;
	private int colorIndex = 0;

	public RainbowArmorTask(Main owner){
		super(owner);
	}

	public void onRun(int currentTick){
		colorIndex++;
		if(colorIndex > 15){
			colorIndex = 0;
		}

		for(Player player : getOwner().getServer().getOnlinePlayers().values()){
			PlayerInventory inventory = player.getInventory();
			int r = new Random().nextInt(255);
			int g = new Random().nextInt(255);
			int b = new Random().nextInt(255);

			if(inventory.getHelmet().getId() == Item.LEATHER_CAP){
				player.getInventory().setHelmet(((ItemColorArmor) inventory.getHelmet()).setColor(r, g, b));
			}

			if(inventory.getChestplate().getId() == Item.LEATHER_TUNIC){
				player.getInventory().setChestplate(((ItemColorArmor) inventory.getChestplate()).setColor(r, g, b));
			}

			if(inventory.getLeggings().getId() == Item.LEATHER_PANTS){
				player.getInventory().setLeggings(((ItemColorArmor) inventory.getLeggings()).setColor(r, g, b));
			}

			if(inventory.getBoots().getId() == Item.LEATHER_BOOTS){
				player.getInventory().setBoots(((ItemColorArmor) inventory.getBoots()).setColor(r, g, b));
			}
		}
	}

}