package game.main.object.entity.event;

import game.main.object.entity.Entity;

public class CollisionEvent extends EntityEvent {
    public Entity other;

    public CollisionEvent(Entity e, Entity other) {
        super(e);
        this.other = other;
    }
}
