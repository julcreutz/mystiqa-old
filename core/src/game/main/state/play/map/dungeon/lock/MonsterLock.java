package game.main.state.play.map.dungeon.lock;

public class MonsterLock extends Lock {
    @Override
    public boolean isLocked() {
        return parent.monsters.size > 0;
    }
}
