package game.main.positionable.entity.event;

import game.main.positionable.entity.Entity;

public class BlockEvent extends EntityEvent {
    public Entity blocked;

    public BlockEvent(Entity e, Entity blocked) {
        super(e);
        this.blocked = blocked;
    }
}
