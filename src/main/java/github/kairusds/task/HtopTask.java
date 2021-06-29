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
		for(Player player : getServer().getOnlinePlayers().values()){
			String tpsColor = "§a";
			Server server = getOwner().getServer();
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

			String msg = "§7TPS: " + tpsColor + NukkitMath.round(tps, 2) + " §8||| §7Load: " + tpsColor + server.getTickUsage() + "§r\n";
			Strig msg = "§7Memory: " + usageColor + usedMB + "§8/§b" + totalMB + "MB " + usageColor + NukkitMath.round(usage, 2) + "§7%";
			player.sendActionBar(msg);
		}
	}

}