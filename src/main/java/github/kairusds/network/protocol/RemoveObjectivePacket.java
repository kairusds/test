package github.kairusds.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import lombok.ToString;

@ToString
public class RemoveObjectivePacket extends DataPacket{

	public static final byte NETWORK_ID = 0x6a;

	public String objectiveName;

	@Override
	public byte pid(){
		return NETWORK_ID;
	}

	@Override
	public void decode() {
		objectiveName = getString();
	}

	@Override
	public void encode() {
		reset();
		putString(objectiveName);
	}
}
