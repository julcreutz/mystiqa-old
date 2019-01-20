package game.main.state.play.map.dungeon.lock;

import com.badlogic.gdx.utils.Array;
import game.main.Game;
import game.main.item.Item;
import game.main.state.play.map.entity.Entity;
import game.main.state.play.map.entity.ItemDrop;
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

        door.spriteSheet = dungeon.doorKey;

        key = Game.ITEMS.load(dungeon.key);

        Array<Entity> monsters = room.getMonsters();

        if (room.getMonsters().size > 0) {
            monsters.get(Game.RANDOM.nextInt(monsters.size)).inventory.add(key);
        } else {
            ItemDrop drop = new ItemDrop(key);

            drop.x = room.getCenterX() * 8;
            drop.y = room.getCenterY() * 8;

            dungeon.entities.addEntity(drop);
        }
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
                door.spriteSheet = dungeon.doorNoKey;
                e.e.inventory.removeValue(key, true);
            }
        }
    }
}
