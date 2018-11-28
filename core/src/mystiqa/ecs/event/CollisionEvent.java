package mystiqa.ecs.event;

import mystiqa.ecs.entity.Entity;

public class CollisionEvent extends EntityEvent<CollisionListener> {
    public Entity _e;

    public CollisionEvent(Entity e, Entity _e) {
        super(e);
        this._e = _e;
    }

    @Override
    public Class<CollisionListener> getListener() {
        return CollisionListener.class;
    }

    @Override
    public void call(CollisionListener listener) {
        listener.onCollision(this);
    }
}
