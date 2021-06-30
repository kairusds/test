package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import github.kairusds.Main;
import github.kairusds.manager.HtopManager;

public class HtopCommand extends Command implements PluginIdentifiableCommand{

	private Main plugin;

	public HtopCommand(Main main){
		super("htop", "toggle server status hud");
		plugin = main;
		setPermission("kairusds.command.htop");
	}

	@Override
	public Main getPlugin() {
		return plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args){
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