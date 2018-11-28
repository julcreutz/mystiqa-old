package mystiqa.ecs.event;

public interface CollisionListener extends EntityListener {
    public void onCollision(CollisionEvent e);
}
