package game.main.state.play.map.lock;

import game.main.state.play.map.Map;
import game.main.state.play.map.Dungeon;
import game.main.entity.Door;
import game.main.entity.event.EntityEvent;
import game.main.entity.event.EntityListener;
import game.main.state.play.map.lock.key.BossKeyLock;
import game.main.state.play.map.lock.key.KeyLock;

public abstract class Lock implements EntityListener {
    public enum Type {
        KILL_MONSTER, KEY, BOSS_KEY, PUSH_BLOCK, INVISIBLE_DOOR;

        public Lock newInstance() {
            switch (this) {
                case KILL_MONSTER:
                    return new KillMonsterLock();
                case KEY:
                    return new KeyLock();
                case BOSS_KEY:
                    return new BossKeyLock();
                case PUSH_BLOCK:
                    return new PushBlockLock();
                case INVISIBLE_DOOR:
                    return new InvisibleDoorLock();
            }

            return null;
        }
    }

    /** Holds map reference. */
    public Map dungeon;

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

    public boolean isValid(Dungeon.Template t) {
        return true;
    }

    public abstract boolean isLocked();

    @Override
    public void eventReceived(EntityEvent e) {

    }
}
