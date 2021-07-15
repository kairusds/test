package github.kairusds.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Position;
import github.kairusds.Main;
import java.util.Arrays;

public class SummonCommand extends BaseCommand{

	public SummonCommand(Main plugin){
		super(plugin, "summon", "summon a nukkit entity", "/summon <entityType> [spawnPos: x y z] [nameTag]");
		commandParameters.clear();
		commandParameters.put("default", new CommandParameter[]{
			CommandParameter.newType("entityType", CommandParamType.STRING), // will change to entity_list once it becomes available 
			CommandParameter.newType("spawnPos", true, CommandParamType.POSITION), // too lazy to implement spawn event
			CommandParameter.newType("nameTag", true, CommandParamType.STRING)
		});
		setPermission("kairusds.command.summon");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args){
		super.execute(sender, commandLabel, args);
		if(!sender.isPlayer()){
			sender.sendMessage("no console allowed");
			return true;
		}

		if(args.length < 1){
			sender.sendMessage("§7Usage: §e" + usageMessage);
			return true;
		}

		// src: https://github.com/PetteriM1/NukkitPetteriM1Edition/blob/23e1c6bbd9a42035bf7c25eca0a301e57dea2167/src/main/java/cn/nukkit/command/defaults/SummonCommand.java#L35
		String mob = Character.toUpperCase(args[0].charAt(0)) + args[0].substring(1);
		int max = mob.length() - 1;
		for(int x = 2; x < max; x++){
			if(mob.charAt(x) == '_'){
				mob = mob.substring(0, x) + Character.toUpperCase(mob.charAt(x + 1)) + mob.substring(x + 2);
			}
		}

		Player player = (Player) sender;
		Entity entity;
		if(args.length < 2){
			if((entity = Entity.createEntity(mob, player)) != null){
				entity.spawnToAll();
				sender.sendMessage("§7Summoned §e" + entity.getName() + " §7at §e" + entity.floor().getX() + "§7, §e" + entity.floor().getY() + "§7, §e" + entity.floor().getZ());
				return true;
			}else{
				sender.sendMessage("§7Unknown entity §c" + args[0]);
				return true;
			}
		}else if(args.length > 1 && args.length < 5){
			double x = Double.parseDouble(args[1].replace("~", String.valueOf(player.getX())));
			double y = Double.parseDouble(args[2].replace("~", String.valueOf(player.getY())));
			double z = Double.parseDouble(args[3].replace("~", String.valueOf(player.getZ())));
			
			if((entity = Entity.createEntity(mob, new Position(x, y, z, player.getLevel()))) != null){
				entity.spawnToAll();
				sender.sendMessage("§7Summoned §e" + entity.getName() + " §7at §e" + entity.floor().getX() + "§7, §e" + entity.floor().getY() + "§7, §e" + entity.floor().getZ());
				return true;
			}else{
				sender.sendMessage("§7Unknown entity §c" + args[0]);
				return true;
			}
		}else if(args.length > 4){
			double x = Double.parseDouble(args[1].replace("~", String.valueOf(player.getX())));
			double y = Double.parseDouble(args[2].replace("~", String.valueOf(player.getY())));
			double z = Double.parseDouble(args[3].replace("~", String.valueOf(player.getZ())));

			if((entity = Entity.createEntity(mob, new Position(x, y, z, player.getLevel()))) != null){
				String[] nameTag = Arrays.copyOfRange(args, 4, args.length);

				entity.setNameTag(String.join(" ", nameTag));
				entity.spawnToAll();
				sender.sendMessage("§7Summoned §e" + entity.getNameTag() + " §7at §e" + entity.floor().getX() + "§7, §e" + entity.floor().getY() + "§7, §e" + entity.floor().getZ());
				return true;
			}else{
				sender.sendMessage("§7Unknown entity §c" + args[0]);
				return true;
			}
		}else{
			sender.sendMessage("§7Usage: §e" + usageMessage);
			return true;
		}
	}

}