package mystiqa.ecs.event;

import mystiqa.ecs.entity.Entity;

public class MoveEvent extends EntityEvent<MoveListener> {
    public float velX;
    public float velY;
    public float velZ;

    public MoveEvent(Entity e, float velX, float velY, float velZ) {
        super(e);

        this.velX = velX;
        this.velY = velY;
        this.velZ = velZ;
    }

    @Override
    public Class<MoveListener> getListener() {
        return MoveListener.class;
    }

    @Override
    public void call(MoveListener listener) {
        listener.onMove(this);
    }
}
