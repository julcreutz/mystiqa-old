package mystiqa.ecs.event;

import mystiqa.ecs.EntityManager;
import mystiqa.ecs.entity.Entity;

public class MoveEvent implements EntityEvent {
    public Entity e;

    public float velX;
    public float velY;
    public float velZ;

    public MoveEvent(Entity e, float velX, float velY, float velZ) {
        this.e = e;

        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
    }

    @Override
    public void sendEvent(EntityManager em) {
        for (MoveListener l : em.getSystems(MoveListener.class)) {
            l.onMove(this);
        }
    }
}
