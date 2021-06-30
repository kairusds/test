package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import github.kairusds.Main;

public class FormCommand extends BaseCommand{

	public FormCommand(Main plugin){
		super(plugin, "form", "show a simple form", "/form <title> <content>");
		commandParameters.clear();
		commandParameters.put("default", new CommandParameter[]{
			CommandParameter.newType("title", CommandParamType.STRING),
			CommandParameter.newType("content", CommandParamType.STRING)
		});
		setPermission("kairusds.command.form");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args){
		super(sender, commandLabel, args);
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