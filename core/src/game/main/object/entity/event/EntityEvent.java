package game.main.object.entity.event;

import game.main.object.entity.Entity;

public abstract class EntityEvent {
    public Entity e;

    public EntityEvent(Entity e) {
        this.e = e;
    }
}
