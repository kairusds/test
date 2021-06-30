package github.kairusds.command;

import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginIdentifiableCommand;
import github.kairusds.Main;

public class BaseCommand extends Command implements PluginIdentifiableCommand{

	protected Main plugin;

	// plugin, name, description, usage, aliases, permission
	public BaseCommand(Main main, String name, String description){
		this(main, name, description, null, new String[0]);
	}

	public BaseCommand(Main main, String name, String description, String usageMessage){
		this(main, name, description, usageMessage, new String[0]);
	}

	public BaseCommand(Main main, String name, String description, String usageMessage, String[] aliases){
		super(name, description, usageMessage, aliases);
		plugin = main;
   }

	@Override
	public Main getPlugin(){
		return plugin;
	}

	protected Server getServer(){
		return plugin.getServer();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args){
		if(!testPermission(sender)){
			return true;
		}
		return true;
	}
}