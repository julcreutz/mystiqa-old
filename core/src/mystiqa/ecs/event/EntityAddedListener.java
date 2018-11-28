package mystiqa.ecs.event;

public interface EntityAddedListener extends EntityListener {
    public void onAdded(EntityAddedEvent e);
}
