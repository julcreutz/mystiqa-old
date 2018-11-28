package mystiqa.ecs.event;

public interface MoveListener extends EntityListener {
    void onMove(MoveEvent e);
}
