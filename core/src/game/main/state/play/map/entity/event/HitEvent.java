package game.main.state.play.map.entity.event;

import game.main.state.play.map.entity.Entity;

public class HitEvent extends EntityEvent {
    public Entity by;

    public HitEvent(Entity e, Entity by) {
        super(e);
        this.by = by;
    }
}
