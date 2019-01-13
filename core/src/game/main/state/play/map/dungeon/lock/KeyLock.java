package game.main.state.play.map.dungeon.lock;

import game.main.Game;
import game.main.item.Item;
import game.main.state.play.map.entity.Entity;

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

        dungeon.player.collisionListeners.add(new Entity.CollisionListener() {
            @Override
            public void onCollision(Entity e) {
                if (e == door && dungeon.player.inventory.contains(key, true)) {
                    locked = false;
                    dungeon.player.inventory.removeValue(key, true);
                }
            }
        });
    }

    @Override
    public boolean isLocked() {
        return locked;
    }
}
