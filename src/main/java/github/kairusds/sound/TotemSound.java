package github.kairusds.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

public class TotemSound extends LevelEventSound {

    public TotemSound(Vector3 pos) {
        this(pos, 0);
    }

    public TotemSound(Vector3 pos, float pitch) {
        super(pos, 1052, pitch);
    }
}
