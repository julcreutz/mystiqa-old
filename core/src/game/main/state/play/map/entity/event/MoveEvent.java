package game.main.state.play.map.entity.event;

import game.main.state.play.map.entity.Entity;

public class MoveEvent extends EntityEvent {
    public MoveEvent(Entity e) {
        super(e);
    }
}
