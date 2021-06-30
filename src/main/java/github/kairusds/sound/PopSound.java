package github.kairusds.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.LevelEventPacket;

public class PopSound extends LevelEventSound {

    public PopSound(Vector3 pos) {
        this(pos, 0);
    }

    public PopSound(Vector3 pos, float pitch) {
        super(pos, 1030, pitch);
    }
}
