package game.main.state.play.map.dungeon.lock;

public class KillMonsterLock extends Lock {
    @Override
    public boolean isLocked() {
        return room.getMonsterCount() > 0;
    }
}
