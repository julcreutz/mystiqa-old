package game.main.state.play.map.dungeon.lock.key;

import com.badlogic.gdx.utils.Array;
import game.main.Game;
import game.main.object.item.Item;
import game.main.object.entity.Entity;
import game.main.object.entity.ItemDrop;
import game.main.object.entity.event.CollisionEvent;
import game.main.object.entity.event.EntityEvent;
import game.main.state.play.map.dungeon.lock.Lock;

public class KeyLock extends Lock {
    public Item key;
    public boolean locked;

    public KeyLock() {
        locked = true;
    }

    public String getKey() {
        return dungeon.key;
    }

    @Override
    public void onLock() {
        super.onLock();

        door.spriteSheet = door.key;

        key = Game.ITEMS.load(getKey());

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

        if (locked) {
            if (e instanceof CollisionEvent) {
                if (e.e == dungeon.player && ((CollisionEvent) e).other == door) {
                    for (int i = 0; i < dungeon.player.inventory.size; i++) {
                        Item item = dungeon.player.inventory.get(i);

                        if (item.id.equals(key.id)) {
                            locked = false;
                            door.spriteSheet = door.noKey;
                            e.e.inventory.removeIndex(i);

                            break;
                        }
                    }
                }
            }
        }
    }
}
