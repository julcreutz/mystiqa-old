package mystiqa.ecs.event;

public interface GroundCollisionListener extends EntityListener {
    public void onGroundCollision(GroundCollisionEvent e);
}
