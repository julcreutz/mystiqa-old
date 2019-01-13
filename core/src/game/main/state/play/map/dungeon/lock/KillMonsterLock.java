package game.main.state.play.map.dungeon.lock;

public class KillMonsterLock extends Lock {
    @Override
    public boolean isLocked() {
        return room.monsters.size > 0;
    }

    @Override
    public boolean takeSameRoom() {
        return true;
    }
}
