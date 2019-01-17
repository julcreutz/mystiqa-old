package game.main.state.play.map.entity.event;

import game.main.state.play.map.entity.Entity;

public class BlockEvent extends EntityEvent {
    public Entity blocked;

    public BlockEvent(Entity e, Entity blocked) {
        super(e);
        this.blocked = blocked;
    }
}
