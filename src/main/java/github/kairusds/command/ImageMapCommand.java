package github.kairusds.command;

import cn.nukkit.Player;
import github.kairusds.Main;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.item.ItemMap;
import cn.nukkit.inventory.Inventory;
import java.net.URL;
import java.net.HttpURLConnection;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class ImageMapCommand extends Command implements PluginIdentifiableCommand{

	private Main plugin;

	public ImageMapCommand(Main plugin){
		super("imagemap", "get a map with a custom image from a url", "/imgmap <url>", new String[]{"im"});
		setPermission("test.command.imagemap");
		plugin = plugin;
		commandParameters.clear();
		commandParameters.put("imagemap", new CommandParameter[]{
			CommandParameter.newType("url", CommandParamType.STRING)
		});
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
		}
		if(args.length < 1){
			sender.sendMessage("Usage: " + usageMessage);
			return true;
		}

		ItemMap map = new ItemMap();
		Player player = (Player) sender; // fuck maven compiler
		Inventory inventory = player.getInventory();

		try{
			sender.sendMessage("Getting url...");
			URL url = new URL(args[0]);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("HEAD");

			if(connection.getResponseCode() != 200){
				sender.sendMessage("Failed. Cannot fetch image from URL.");
				return true;
			}

			BufferedImage image = ImageIO.read(url);
			map.setImage(image);
			connection.disconnect();
		}catch(Exception error){
			sender.sendMessage("Failure.");
		}
		
		if(inventory.canAddItem(map)){
			inventory.addItem(map);
		}
	}

}