package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.level.Level;
import github.kairusds.Main;
import java.util.ArrayList;

public class WorldCommand extends BaseCommand{

	public WorldCommand(Main plugin){
		super(plugin, "world", "teleport to a world or list available ones", "/world [worldName]");
		commandParameters.clear();
		commandParameters.put("default", new CommandParameter[]{
			CommandParameter.newType("worldName", CommandParamType.STRING)
		});
		setPermission("kairusds.command.world");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args){
		super(sender, commandLabel, args);
		if(!sender.isPlayer()){
			sender.sendMessage("no console allowed");
			return true;
		}

		if(args.length < 1){
			ArrayList<String> worlds = new ArrayList<>();
			for(Level level : getServer().getLevels().values()){
				worlds.add(level.getName());
			}
			sender.sendMessage("§7Available worlds: §e" + String.join("§8, §e", worlds));
			return true;
		}

		Level level = getServer().getLevelByName(args[0]);
		if(level == null){
			sender.sendMessage("§7World does not exist.");
			return true;
		}

		if(!getServer().loadLevel(args[0])){
			sender.sendMessage("§cFailed to load the world.");
			return true;
		}

		sender.sendMessage("§7Teleporting to §e" + args[0] + "§7...");
		((Player) sender).teleport(level.getSafeSpawn());
		return true;
	}

}