package game.main.item.equipment;

import game.main.entity.event.EntityEvent;
import game.main.entity.event.EntityListener;
import game.main.item.Item;

public abstract class Equipment extends Item implements EntityListener {
    @Override
    public void eventReceived(EntityEvent e) {
    }
}
