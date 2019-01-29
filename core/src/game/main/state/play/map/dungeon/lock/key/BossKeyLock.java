package game.main.state.play.map.dungeon.lock.key;

public class BossKeyLock extends KeyLock {
    @Override
    public void onLock() {
        super.onLock();

        door.spriteSheet = door.bossKey;
    }
}
