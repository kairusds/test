package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import github.kairusds.Main;
import github.kairusds.Permissions;

public class FormCommand extends Command implements PluginIdentifiableCommand{

	private Main plugin;

	public FormCommand(Main main){
		super("form", "show a simple form", "/form <title> <content>");
		plugin = main;
		setPermission(Permissions.COMMAND_FORM);
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

		if(args.length < 2){
			sender.sendMessage("§7" + usageMessage);
		}

		plugin.getFormManager().sendSimpleForm((Player) sender, args[0], args[1]);
		return true;
	}

}