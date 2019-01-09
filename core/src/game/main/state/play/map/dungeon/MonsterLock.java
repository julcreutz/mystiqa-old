package game.main.state.play.map.dungeon;

public class MonsterLock extends Lock {
    @Override
    public boolean isLocked() {
        return room.monsters.size > 0;
    }
}
