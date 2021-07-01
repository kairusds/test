package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import github.kairusds.Main;
import github.kairusds.manager.ImageMapManager;

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
		manager.addUser(player);
		return true;
	}

}