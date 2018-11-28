package mystiqa.ecs.event;

import mystiqa.ecs.entity.Entity;

public class EntityAddedEvent extends EntityEvent<EntityAddedListener> {
    public EntityAddedEvent(Entity e) {
        super(e);
    }

    @Override
    public Class<EntityAddedListener> getListener() {
        return EntityAddedListener.class;
    }

    @Override
    public void call(EntityAddedListener listener) {
        listener.onAdded(this);
    }
}
