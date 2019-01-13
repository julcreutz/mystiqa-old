package game.main.state.play.map.dungeon.lock;

import game.main.state.play.map.dungeon.Dungeon;
import game.main.state.play.map.entity.Door;

public abstract class Lock {
    public enum Type {
        KILL_MONSTER,
        KEY,
        PUSH_BLOCK;

        public Lock newInstance() {
            switch (this) {
                case KILL_MONSTER:
                    return new KillMonsterLock();
                case KEY:
                    return new KeyLock();
                case PUSH_BLOCK:
                    return new PushBlockLock();
            }

            return null;
        }
    }

    /** Holds dungeon reference. */
    public Dungeon dungeon;

    /** The room unlocking this lock. */
    public Dungeon.Room room;
    /** The door unlocked by lock. */
    public Door door;

    public void update() {
        // Lock and open doors according to lock status
        if (isLocked() && !door.visible) {
            door.visible = true;
        } else if (!isLocked() && door.visible) {
            door.visible = false;
        }
    }

    public void onLock() {
    }

    public abstract boolean isLocked();

    public boolean takeSameRoom() {
        return false;
    }
}
