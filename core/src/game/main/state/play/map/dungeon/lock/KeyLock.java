package game.main.state.play.map.dungeon.lock;

import game.main.Game;
import game.main.item.Item;
import game.main.state.play.map.dungeon.Dungeon;
import game.main.state.play.map.entity.event.CollisionEvent;
import game.main.state.play.map.entity.event.EntityEvent;

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
    public boolean isRoomValid(Dungeon.Room r) {
        return r.monsters.size > 0;
    }

    @Override
    public boolean isLocked() {
        return locked;
    }

    @Override
    public void eventReceived(EntityEvent e) {
        super.eventReceived(e);

        if (e instanceof CollisionEvent) {
            if (e.e == dungeon.player && ((CollisionEvent) e).other == door && e.e.inventory.contains(key, true)) {
                locked = false;
                e.e.inventory.removeValue(key, true);
            }
        }
    }
}
