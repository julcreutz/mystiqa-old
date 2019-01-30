package game.main.item.equipment;

import game.main.positionable.entity.event.EntityEvent;
import game.main.positionable.entity.event.EntityListener;
import game.main.item.Item;

public abstract class Equipment extends Item implements EntityListener {
    @Override
    public void eventReceived(EntityEvent e) {
    }
}
