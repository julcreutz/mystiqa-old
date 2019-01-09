package game.main.state.play.map.dungeon.lock;

import game.main.state.play.map.dungeon.Dungeon;
import game.main.state.play.map.entity.Door;

public abstract class Lock {
    public Dungeon.Room parent;
    public Dungeon.Room child;

    public Door door;

    public void update() {
        if (parent.containsPlayer()) {
            if (!isLocked() && door.visible) {
                door.visible = false;
            } else if (isLocked() && !door.visible) {
                door.visible = true;
            }
        }
    }

    public abstract boolean isLocked();
}
