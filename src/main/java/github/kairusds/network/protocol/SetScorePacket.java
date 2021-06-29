package github.kairusds.network.protocol;

import cn.nukkit.network.protocol.DataPacket;
import lombok.ToString;
import java.util.List;

@ToString
public class SetScorePacket extends DataPacket{
	
	public static final byte NETWORK_ID = 0x6c;

	public static final int TYPE_CHANGE = 0;
	public static final int TYPE_REMOVE = 1;

	public int type;
	public List<Entry> entries = new List<>();

	@Override
	public void decode(){
		type = getByte();
		for(int i = 0;  i < (int) getUnsignedVarInt(); ++i){
			Entry entry = new Entry();
			entry.scoreboardId = getVarLong();
			entry.objectiveName = getString();
			entry.score = getLInt();
			if(type != TYPE_REMOVE){
				entry.type = getByte();
				switch(entry.type){
					case Entry.TYPE_PLAYER:
					case Entry.TYPE_ENTITY:
						entry.entityUniqueId = getEntityUniqueId();
						break;
					case Entry.TYPE_FAKE_PLAYER:
						entry.customName = getString();
						break;
					default:
						throw new IllegalArgumentException("Unknown entry type entry.type");
				}
			}
			entries.add(entry);
		}
	}

	protected function encodePayload(){
		putByte(type);
		putUnsignedVarInt(entries.size());
		for(Entry entry : entries){
			putVarLong(entry.scoreboardId);
			putString(entry.objectiveName);
			putLInt(entry.score);
			if(type != TYPE_REMOVE){
				putByte(entry.type);
				switch(entry.type){
					case Entry.TYPE_PLAYER:
					case Entry.TYPE_ENTITY:
						putEntityUniqueId(entry.entityUniqueId);
						break;
					case Entry.TYPE_FAKE_PLAYER:
						putString(entry.customName);
						break;
					default:
						throw new IllegalArgumentException("Unknown entry type entry.type");
				}
			}
		}
	}

	@ToString
	public static class Entry{
		public static final int TYPE_PLAYER = 1;
		public static final int TYPE_ENTITY = 2;
		public static final int TYPE_FAKE_PLAYER = 3;

		public int scoreboardId;
		public String objectiveName;
		public int $score;
		public int $type;

		// another hack ig
		public Object entityUniqueId;
		public Object customName;
	}
}