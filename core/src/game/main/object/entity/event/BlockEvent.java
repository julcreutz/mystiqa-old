package game.main.object.entity.event;

import game.main.object.entity.Entity;

public class BlockEvent extends EntityEvent {
    public Entity blocked;

    public BlockEvent(Entity e, Entity blocked) {
        super(e);
        this.blocked = blocked;
    }
}
