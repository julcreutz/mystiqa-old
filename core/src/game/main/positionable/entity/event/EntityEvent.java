package game.main.positionable.entity.event;

import game.main.positionable.entity.Entity;

public abstract class EntityEvent {
    public Entity e;

    public EntityEvent(Entity e) {
        this.e = e;
    }
}
