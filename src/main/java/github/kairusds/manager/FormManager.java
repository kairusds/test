package github.kairusds.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.element.Element;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowModal;
import cn.nukkit.form.window.FormWindowSimple;
import github.kairusds.Main;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// api kinda wack
public class FormManager{

	private Main plugin;

	public FormManager(Main main){
		plugin = main;
	}

	public int sendCustomForm(Player player, String title) {
		return sendCustomForm(player, title, new ArrayList<>());
	}

	public int sendCustomForm(Player player, String title, List<Element> contents) {
		return sendCustomForm(player, title, contents, (ElementButtonImageData) null);
	}

	public int sendCustomForm(Player player, String title, List<Element> contents, String icon) {
		return sendCustomForm(player, title, contents, icon.isEmpty() ? null : new ElementButtonImageData(ElementButtonImageData.IMAGE_DATA_TYPE_URL, icon));
	}

	public int sendCustomForm(Player player, String title, List<Element> contents, ElementButtonImageData icon) {
		FormWindowCustom form = new FormWindowCustom(String title, List<Element> contents, ElementButtonImageData icon);
		return player.showFormWindow(form);
	}

	public int sendModalForm(Player player, String title, String content, String button, String button1){
		FormWindowModal form = new FormWindowModal(title, content, button, button1);
		return player.showFormWindow(form);
	}

	public int sendSimpleForm(Player player, String title, String content){
		return sendSimpleForm(player, title, content, new ArrayList<>());
	}

	public int sendSimpleForm(Player player, String title, String content, List<ElementButton> buttons){
		FormWindowSimple form = new FormWindowSimple(title, content, buttons);
		if(buttons.isEmpty()) form.addButton(new ElementButton("Close"));
		return player.showFormWindow(form);
	}

}