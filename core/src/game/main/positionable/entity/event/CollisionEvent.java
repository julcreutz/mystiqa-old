package game.main.positionable.entity.event;

import game.main.positionable.entity.Entity;

public class CollisionEvent extends EntityEvent {
    public Entity other;

    public CollisionEvent(Entity e, Entity other) {
        super(e);
        this.other = other;
    }
}
