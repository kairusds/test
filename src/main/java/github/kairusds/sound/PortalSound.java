package github.kairusds.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.level.sound.LevelEventSound;

public class PortalSound extends LevelEventSound {

    public PortalSound(Vector3 pos) {
        this(pos, 0);
    }

    public PortalSound(Vector3 pos, float pitch) {
        super(pos, 1032, pitch);
    }
}
