package mystiqa.ecs.event;

import mystiqa.ecs.EntityManager;
import mystiqa.ecs.entity.Entity;

public class EntityAddedEvent implements EntityEvent {
    public Entity e;

    public EntityAddedEvent(Entity e) {
        this.e = e;
    }

    @Override
    public void sendEvent(EntityManager em) {
        for (EntityAddedListener l : em.getSystems(EntityAddedListener.class)) {
            l.onAdded(this);
        }
    }
}
