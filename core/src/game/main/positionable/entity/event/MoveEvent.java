package game.main.positionable.entity.event;

import game.main.positionable.entity.Entity;

public class MoveEvent extends EntityEvent {
    public MoveEvent(Entity e) {
        super(e);
    }
}
