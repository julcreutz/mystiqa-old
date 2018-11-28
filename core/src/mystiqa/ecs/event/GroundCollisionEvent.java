package mystiqa.ecs.event;

import mystiqa.ecs.EntityManager;
import mystiqa.ecs.entity.Entity;

public class GroundCollisionEvent implements EntityEvent {
    public Entity e;

    public GroundCollisionEvent(Entity e) {
        this.e = e;
    }

    @Override
    public void sendEvent(EntityManager em) {
        for (GroundCollisionListener l : em.getSystems(GroundCollisionListener.class)) {
            l.onGroundCollision(this);
        }
    }
}
