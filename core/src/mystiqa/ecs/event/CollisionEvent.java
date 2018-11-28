package mystiqa.ecs.event;

import mystiqa.ecs.EntityManager;
import mystiqa.ecs.entity.Entity;

public class CollisionEvent implements EntityEvent {
    public Entity e;
    public Entity _e;

    public CollisionEvent(Entity e, Entity _e) {
        this.e = e;
        this._e = _e;
    }

    @Override
    public void sendEvent(EntityManager em) {
        for (CollisionListener l : em.getSystems(CollisionListener.class)) {
            l.onCollision(this);
        }
    }
}
