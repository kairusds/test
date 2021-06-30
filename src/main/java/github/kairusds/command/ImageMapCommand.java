package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.item.ItemMap;
import cn.nukkit.inventory.Inventory;
import github.kairusds.Main;
import java.net.URL;
import java.net.HttpURLConnection;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageMapCommand extends Command implements PluginIdentifiableCommand{

	private Main plugin;

	public ImageMapCommand(Main main){
		super("imagemap", "get a map with a custom image", null, new String[]{"im"});
		plugin = main;
		setPermission("kairusds.command.imagemap");
	}

	@Override
	public Main getPlugin() {
		return plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args){
		if(!this.testPermission(sender)){
			return true;
		}
		if(!sender.isPlayer()){
			sender.sendMessage("no console allowed");
			return true;
		}

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
		return true;
	}

}