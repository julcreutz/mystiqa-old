package game.main.positionable.entity.event;

import game.main.positionable.entity.Entity;

public class HitEvent extends EntityEvent {
    public Entity by;

    public HitEvent(Entity e, Entity by) {
        super(e);
        this.by = by;
    }
}
