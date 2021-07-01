package github.kairusds.manager;

import cn.nukkit.Player;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementInput;
import github.kairusds.Main;
import java.util.ArrayList;
import java.util.UUID;

public class ImageMapManager{

	private Main plugin;

	private ArrayList<UUID> users = new ArrayList<>();

	public ImageMapManager(Main main){
		plugin = main;
	}

	public boolean isUser(Player player){
		return users.contains(player.getUniqueId());
	}

	public void addUser(Player player){
		ArrayList<Element> contents = new ArrayList<>();
		contents.add(new ElementInput("this is text yeah?", "Placeholder", "https://placeimg.com/128/128/tech"));
		plugin.getFormManager().sendCustomForm(player, "Image Map", contents);
		users.add(player.getUniqueId());
	}

	public void removeUser(Player player){
		users.remove(users.indexOf(player.getUniqueId()));
	}

}