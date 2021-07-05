// skin - x
// cape - x
package github.kairusds.manager;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.element.ElementToggle;
import github.kairusds.Main;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.UUID;

public class SettingsManager{

	private Main plugin;

	private ArrayList<UUID> users = new ArrayList<>();

	public SettingsManager(Main main){
		plugin = main;
	}

	public boolean isUser(Player player){
		return users.contains(player.getUniqueId());
	}

	public void addUser(Player player){
		ArrayList<Element> contents = new ArrayList<>();
		ArrayList<String> gamemodes = Arrays.asList("Survival", "Creative", "Adventure", "Spectator");

		contents.add(new ElementInput("", "Display Name", player.getDisplayName()));
		contents.add(new ElementInput("", "Nametag", player.getNameTag()));
		contents.add(new ElementDropdown("Gamemode", gamemodes, player.getGamemode()));
		contents.add(new ElementToggle("Invisible", player.getDataFlag(Entity.DATA_FLAGS, Entity.DATA_FLAG_INVISIBLE)));

		if(player.isSurvival() || player.isAdventure()){
			contents.add(new ElementToggle("Hunger", player.isFoodEnabled()));
			contents.add(new ElementToggle("Flight", player.getAllowFlight()));
		}

		plugin.getFormManager().sendCustomForm(player, "Player Settings", contents);
		users.add(player.getUniqueId());
	}

	public void removeUser(Player player){
		users.remove(users.indexOf(player.getUniqueId()));
	}

}