package github.kairusds.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import lombok.ToString;
import java.util.List;
import java.util.ArrayList;

@ToString
public class SetScoreboardIdentityPacket extends DataPacket{

	public static final byte NETWORK_ID = 0x70;

	public static final int TYPE_REGISTER_IDENTITY = 0;
	public static final int TYPE_CLEAR_IDENTITY = 1;

	public int type;
	public List<Entry> entries = new ArrayList<>();

	@Override
	public void decode(){
		type = getByte();
		for(int i = 0, i2 = (int) getUnsignedVarInt();  i < i2; ++i){
			Entry entry = new Entry();
			entry.scoreboardId = getVarLong();
			if(type == TYPE_REGISTER_IDENTITY){
				entry.entityUniqueId = getEntityUniqueId();
			}
			entries.add(entry);
		}
	}

	@Override
	public void encode(){
		putByte((byte) type);
		putUnsignedVarInt(entries.size());
		for(Entry entry : entries){
			putVarLong(entry.scoreboardId);
			if(type == TYPE_REGISTER_IDENTITY){
				putEntityUniqueId(entry.entityUniqueId);
			}
		}
	}

	@ToString
	public static class Entry{
		public long scoreboardId;
		public long entityUniqueId;
	}
}