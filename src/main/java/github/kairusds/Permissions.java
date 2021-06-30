package github.kairusds;

// permission manager soon idk
public enum Permissions{
	COMMAND_FORM("kairusds.command.form"),
	COMMAND_HTOP("kairusds.command.htop"),
	COMMAND_IMAGE_MAP("kairusds.command.imagemap"),
	DEVICE_INFO_MESSAGE("kairusds.message.device");

	private String name;

	public String toString(){
		return this.name;
	}

	Permissions(String name){
		this.name = name;
	}
}