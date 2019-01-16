package game.main.state.play.map.dungeon.lock;

import game.main.state.play.map.dungeon.Dungeon;

public class KillMonsterLock extends Lock {
    @Override
    public boolean isRoomValid(Dungeon.Room r) {
        return true;
    }

    @Override
    public boolean isLocked() {
        return room.monsters.size > 0;
    }
}
