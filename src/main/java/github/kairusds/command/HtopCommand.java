package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import github.kairusds.Main;
import github.kairusds.manager.HtopManager;

public class HtopCommand extends BaseCommand{

	public HtopCommand(Main plugin){
		super(plugin, "htop", "toggle server status hud");
		setPermission("kairusds.command.htop");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args){
		super(sender, commandLabel, args);
		if(!sender.isPlayer()){
			sender.sendMessage("no console allowed");
			return true;
		}

		Player player = (Player) sender;
		HtopManager manager = plugin.getHtopManager();
		if(manager.isUser(player)){
			sender.sendMessage("§7Disabled htop.");
			manager.removeUser(player);
			return true;
		}

		sender.sendMessage("§aEnabled htop.");
		manager.addUser(player);
		return true;
	}

}