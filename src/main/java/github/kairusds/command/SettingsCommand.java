package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import github.kairusds.Main;
import github.kairusds.manager.SettingsManager;

public class SettingsCommand extends BaseCommand{

	public SettingsCommand(Main plugin){
		super(plugin, "settings", "change player settings", null, new String[]{"opt"});
		setPermission("kairusds.command.settings");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args){
		super.execute(sender, commandLabel, args);
		if(!sender.isPlayer()){
			sender.sendMessage("no console allowed");
			return true;
		}

		Player player = (Player) sender;
		SettingsManager manager = plugin.getSettingsManager();
		if(manager.isUser(player)) return true;
		manager.addUser(player);
		return true;
	}

}