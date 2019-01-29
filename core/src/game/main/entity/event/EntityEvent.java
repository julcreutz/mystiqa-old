package game.main.entity.event;

import game.main.entity.Entity;

public abstract class EntityEvent {
    public Entity e;

    public EntityEvent(Entity e) {
        this.e = e;
    }
}
