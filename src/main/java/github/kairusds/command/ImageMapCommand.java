package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import github.kairusds.Main;
import github.kairusds.manager.ImageMapManager;
/*
import cn.nukkit.item.ItemMap;
import cn.nukkit.inventory.Inventory;
import java.net.URL;
import java.net.HttpURLConnection;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;*/

public class ImageMapCommand extends BaseCommand{

	public ImageMapCommand(Main plugin){
		super(plugin, "imagemap", "get a map with a custom image", null, new String[]{"im"});
		setPermission("kairusds.command.imagemap");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args){
		super.execute(sender, commandLabel, args);
		if(!sender.isPlayer()){
			sender.sendMessage("no console allowed");
			return true;
		}

		Player player = (Player) sender;
		ImageMapManager manager = plugin.getImageMapManager();
		if(manager.isUser(player)) return true; // spamming the command?
		manager.addUser(sender);

		/* to be moved
		ItemMap map = new ItemMap();
		Inventory inventory = ((Player) sender).getInventory(); // fuck java for this unholy solution

		try{
			sender.sendMessage("§7Fetching image...");
			URL url = new URL(args[0]);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");

			if(connection.getResponseCode() != 200){
				sender.sendMessage("§cImage URL is inaccessible.");
				return true;
			}

			BufferedImage image = ImageIO.read(url);
			map.setImage(image);
			sender.sendMessage("§bImage Map §ahas been successfully created.");
			connection.disconnect();
		}catch(Exception error){
			sender.sendMessage("§cA fatal error has occured, check the server console for more details.");
			plugin.getServer().getLogger().error(error.toString());
		}
		
		if(inventory.canAddItem(map)){
			inventory.addItem(map);
		}
		*/
		return true;
	}

}