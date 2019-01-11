package game.main.state.play.map.dungeon.lock;

import game.main.Game;
import game.main.item.Item;

public class KeyLock extends Lock {
    public Item key;
    public boolean locked;

    public KeyLock() {
        locked = true;
    }

    @Override
    public void onLock() {
        super.onLock();

        key = Game.ITEMS.load(dungeon.key);
        room.monsters.get(Game.RANDOM.nextInt(room.monsters.size)).inventory.add(key);
    }

    @Override
    public void update() {
        super.update();

        if (dungeon.player.hitbox.overlaps(door) && dungeon.player.inventory.contains(key, true)) {
            locked = false;
        }
    }

    @Override
    public boolean isLocked() {
        return locked;
    }
}
