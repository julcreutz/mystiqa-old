package mystiqa.ecs.event;

import mystiqa.ecs.EntityManager;

public interface EntityEvent {
    public void sendEvent(EntityManager em);
}
