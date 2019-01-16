package game.main.state.play.map.entity.event;

import game.main.state.play.map.entity.Entity;

public class CollisionEvent extends EntityEvent {
    public Entity other;

    public CollisionEvent(Entity e, Entity other) {
        super(e);
        this.other = other;
    }
}
