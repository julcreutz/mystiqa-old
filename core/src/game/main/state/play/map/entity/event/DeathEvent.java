package game.main.state.play.map.entity.event;

import game.main.state.play.map.entity.Entity;

public class DeathEvent extends EntityEvent {
    public DeathEvent(Entity e) {
        super(e);
    }
}
