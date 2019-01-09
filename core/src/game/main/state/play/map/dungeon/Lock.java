package game.main.state.play.map.dungeon;

import game.main.state.play.map.entity.Door;

public abstract class Lock {
    public Dungeon.Room room;
    public Door door;

    public void update() {
        if (!isLocked() && door.visible) {
            door.visible = false;
        }
    }

    public abstract boolean isLocked();
}
