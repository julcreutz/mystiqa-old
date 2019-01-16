package game.main.state.play.map.dungeon.lock;

import game.main.state.play.map.dungeon.Dungeon;

public class InvisibleDoorLock extends Lock {
    @Override
    public void onLock() {
        super.onLock();

        door.invisible = true;
    }

    @Override
    public boolean isRoomValid(Dungeon.Room r) {
        return true;
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
