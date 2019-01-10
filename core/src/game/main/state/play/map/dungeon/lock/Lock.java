package game.main.state.play.map.dungeon.lock;

import game.main.state.play.map.dungeon.Dungeon;
import game.main.state.play.map.entity.Door;

public abstract class Lock {
    /** Holds dungeon reference. */
    public Dungeon dungeon;

    /** The room unlocking this lock. */
    public Dungeon.Room room;
    /** The door unlocked by lock. */
    public Door door;

    public void update() {
        if (room.containsPlayer()) {
            if (!isLocked() && door.visible) {
                door.visible = false;
            } else if (isLocked() && !door.visible) {
                door.visible = true;
            }
        }
    }

    public void onLock() {

    }

    public abstract boolean isLocked();
}
