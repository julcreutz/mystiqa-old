package mystiqa.ecs.event;

import mystiqa.ecs.EntityManager;
import mystiqa.ecs.entity.Entity;

public abstract class EntityEvent<T extends EntityListener> {
    public Entity e;

    public EntityEvent(Entity e) {
        this.e = e;
    }

    public void sendEvent(EntityManager em) {
        for (T listener : em.getSystems(getListener())) {
            if (em.isEntitySuitable(e, em.getRequiredComponents(listener.getClass()))) {
                call(listener);
            }
        }
    }

    public abstract Class<T> getListener();

    public abstract void call(T listener);
}
