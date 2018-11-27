package mystiqa.ecs.event;

import mystiqa.entity.Entity;

public interface EntityAddedListener extends EntityListener {
    public void onAdded(Entity e);
}
