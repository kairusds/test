package github.kairusds.task;

import github.kairusds.Main;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.math.NukkitMath;
import cn.nukkit.scheduler.PluginTask;
import java.util.ArrayList;

public class HtopTask extends PluginTask<Main>{

	public HtopTask(Main owner){
		super(owner);
	}

	public void onRun(int currentTick){
		Server server = getOwner().getServer();
		for(Player player : server.getOnlinePlayers().values()){
			if(getOwner().getHtopManager().isUser(player)){
				String tpsColor = "§a";
				float tps = server.getTicksPerSecond();
	
				if(tps < 17){
					tpsColor = "§e";
				}else if (tps < 12){
					tpsColor = "§c";
				}
	
				Runtime runtime = Runtime.getRuntime();
				double totalMB = NukkitMath.round(((double) runtime.totalMemory()) / 1024 / 1024, 2);
				double usedMB = NukkitMath.round((double) (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024, 2);
				double maxMB = NukkitMath.round(((double) runtime.maxMemory()) / 1024 / 1024, 2);
				double usage = usedMB / maxMB * 100;
				String usageColor = "§a";
	
				if(usage < 70){
					usageColor = "§e";
				}else if(usage > 70){
					usageColor = "§c";
				}

				StringBuilder msg = new StringBuilder("§7TPS: " + tpsColor + NukkitMath.round(tps, 2) + " §8— §7Load: " + tpsColor + server.getTickUsage() + "§7%§r\n");
				msg.append("§7Memory: " + usageColor + usedMB + "§8/§b" + totalMB + "MB §8(" + usageColor + NukkitMath.round(usage, 2) + "§7%§8)");
				player.sendActionBar(msg);
			}
		}
	}

}