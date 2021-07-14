package github.kairusds;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import github.kairusds.command.*;
import github.kairusds.protocol.*;
import github.kairusds.manager.*;
import github.kairusds.task.HtopTask;
import java.util.ArrayList;
import java.util.UUID;

public class Main extends PluginBase{

	private static Main instance;

	private BlockTrackingManager blockTrackingManager;
	private EntityTrackingManager entityTrackingManager;
	private FormManager formManager;
	private HtopManager htopManager;
	private ImageMapManager imageMapManager;
	private SettingsManager settingsManager;

	@Override
	public void onLoad(){
		getLogger().info(TextFormat.WHITE + "Let's");
	}

	@Override
	public void onEnable(){
		instance = this;
		getLogger().info(TextFormat.DARK_GREEN + "FUCKING GOOOOOOOOO");
		getServer().getPluginManager().registerEvents(new EventListener(this), this);
		registerCommands();
		registerPackets();
		setManagers();
	}

	public static Main getInstance(){
		return instance;
	}

	private void registerCommands(){
		ArrayList<Command> commands = new ArrayList<>();
		commands.add(new FormCommand(this));
		commands.add(new HtopCommand(this));
		commands.add(new ImageMapCommand(this));
		commands.add(new WorldCommand(this));
		commands.add(new SettingsCommand(this));
		getServer().getCommandMap().registerAll("test", commands);
	}

	// idk
	private void registerPackets(){
		getServer().getNetwork().registerPacket(RemoveObjectivePacket.NETWORK_ID, RemoveObjectivePacket.class);
		getServer().getNetwork().registerPacket(SetDisplayObjectivePacket.NETWORK_ID, SetDisplayObjectivePacket.class);
		getServer().getNetwork().registerPacket(SetScoreboardIdentityPacket.NETWORK_ID, SetScoreboardIdentityPacket.class);
		getServer().getNetwork().registerPacket(SetScorePacket.NETWORK_ID, SetScorePacket.class);
	}

	public void setManagers(){
		blockTrackingManager = new BlockTrackingManager(this);
		entityTrackingManager = new EntityTrackingManager(this);
		formManager = new FormManager(this);
		htopManager = new HtopManager(this);
		imageMapManager = new ImageMapManager(this);
		settingsManager = new SettingsManager(this);
	}

	public BlockTrackingManager getBlockTrackingManager(){
		return blockTrackingManager;
	}

	public EntityTrackingManager getEntityTrackingManager(){
		return entityTrackingManager;
	}

	public FormManager getFormManager(){
		return formManager;
	}

	public HtopManager getHtopManager(){
		return htopManager;
	}

	public ImageMapManager getImageMapManager(){
		return imageMapManager;
	}

	public SettingsManager getSettingsManager(){
		return settingsManager;
	}

	@Override
	public void onDisable(){
		getLogger().info(TextFormat.DARK_RED + "deez nuts");
	}
}