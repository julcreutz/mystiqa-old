package game.main.entity.event;

import game.main.entity.Entity;

public class CollisionEvent extends EntityEvent {
    public Entity other;

    public CollisionEvent(Entity e, Entity other) {
        super(e);
        this.other = other;
    }
}
