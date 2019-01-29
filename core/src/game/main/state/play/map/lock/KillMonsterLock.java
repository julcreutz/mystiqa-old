package game.main.state.play.map.lock;

public class KillMonsterLock extends Lock {
    @Override
    public boolean isLocked() {
        return room.getMonsterCount() > 0;
    }
}
