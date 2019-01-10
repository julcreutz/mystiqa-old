package game.main.state.play.map.dungeon.lock;

public class MonsterLock extends Lock {
    @Override
    public boolean isLocked() {
        return room.monsters.size > 0;
    }
}
