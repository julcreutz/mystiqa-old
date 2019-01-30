package game.main.positionable.entity.event;

import game.main.positionable.entity.Entity;

public class DeathEvent extends EntityEvent {
    public DeathEvent(Entity e) {
        super(e);
    }
}
