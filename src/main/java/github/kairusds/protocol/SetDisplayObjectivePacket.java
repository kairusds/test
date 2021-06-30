package github.kairusds.protocol;

import cn.nukkit.network.protocol.DataPacket;
import lombok.ToString;

@ToString
public class SetDisplayObjectivePacket extends DataPacket{

	public static final byte NETWORK_ID = 0x6b;

	public static final String DISPLAY_SLOT_LIST = "list";
	public static final String DISPLAY_SLOT_SIDEBAR = "sidebar";
	public static final String DISPLAY_SLOT_BELOW_NAME = "belowname";

	public static final int SORT_ORDER_ASCENDING = 0;
	public static final int SORT_ORDER_DESCENDING = 1;

	public String displaySlot;
    public String objectiveName;
    public String displayName;
    public String criteriaName;
    public int sortOrder;

	@Override
	public byte pid() {
		return NETWORK_ID;
	}

	@Override
	public void decode() {
		displaySlot = getString();
		objectiveName = getString();
		displayName = getString();
		criteriaName = getString();
		sortOrder = getVarInt();
	}

	@Override
	public void encode() {
		reset();
		putString(displaySlot);
		putString(objectiveName);
		putString(displayName);
		putString(criteriaName);
		putVarInt(sortOrder);
	}
}
