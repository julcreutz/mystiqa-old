package game.main.state.play.map.lock.key;

import com.badlogic.gdx.utils.Array;
import game.main.Game;
import game.main.entity.Entity;
import game.main.item.Item;
import game.main.entity.event.CollisionEvent;
import game.main.entity.event.EntityEvent;
import game.main.item.collectable.Key;
import game.main.state.play.map.lock.Lock;

public class KeyLock extends Lock {
    public Item key;
    public boolean locked;

    public KeyLock() {
        locked = true;
    }

    public Class getKey() {
        return Key.class;
    }

    @Override
    public void onLock() {
        super.onLock();

        try {
            key = (Item) getKey().newInstance();

            door.spriteSheet = door.key;

            Array<Entity> monsters = room.getMonsters();

            if (room.getMonsters().size > 0) {
                monsters.get(Game.RANDOM.nextInt(monsters.size)).inventory.add(key);
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
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

                        if (item.getClass().equals(getKey())) {
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
