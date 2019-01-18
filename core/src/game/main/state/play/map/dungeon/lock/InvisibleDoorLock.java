package game.main.state.play.map.dungeon.lock;

public class InvisibleDoorLock extends Lock {
    @Override
    public void onLock() {
        super.onLock();

        door.invisible = true;
    }

    @Override
    public boolean isLocked() {
        return false;
    }
}
