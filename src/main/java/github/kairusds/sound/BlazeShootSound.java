package github.kairusds.sound;

import cn.nukkit.math.Vector3;
import cn.nukkit.level.sound.LevelEventSound;

public class BlazeShootSound extends LevelEventSound {

    public BlazeShootSound(Vector3 pos) {
        this(pos, 0);
    }

    public BlazeShootSound(Vector3 pos, float pitch) {
        super(pos, 1009, pitch);
    }
}
