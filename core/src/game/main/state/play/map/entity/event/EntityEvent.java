package game.main.state.play.map.entity.event;

import game.main.state.play.map.entity.Entity;

public abstract class EntityEvent {
    public Entity e;

    public EntityEvent(Entity e) {
        this.e = e;
    }
}
