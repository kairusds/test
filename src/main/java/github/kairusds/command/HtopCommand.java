package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.utils.TextFormat;
import github.kairusds.Main;

public class HtopCommand extends Command implements PluginIdentifiableCommand{

	private Main plugin;

	public HtopCommand(Main main){
		super("htop", "toggle server status hud", null, new String[0]);
		setPermission("test.command.htop");
		plugin = main;
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
		if(plugin.isHtopUser(player)){
			sender.sendMessage("Disabled htop.");
			plugin.removeHtopUser(player);
			return true;
		}

		sender.sendMessage("Enabled htop.");
		plugin.addHtopUser(player);
		return true;
	}

}