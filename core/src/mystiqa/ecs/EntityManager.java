package mystiqa.ecs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import mystiqa.ecs.event.EntityAddedEvent;
import mystiqa.ecs.event.EntityEvent;
import mystiqa.ecs.system.EntitySystem;
import mystiqa.ecs.system.Renderable;
import mystiqa.ecs.system.Updateable;
import mystiqa.entity.Entity;

public class EntityManager {
    public Array<EntitySystem> systems;
    public Array<Entity> entities;

    public EntityManager() {
        systems = new Array<EntitySystem>();
        entities = new Array<Entity>();
    }

    public void update() {
        for (Updateable u : getSystems(Updateable.class)) {
            u.update(this);
        }
    }

    public void render(SpriteBatch batch) {
        for (Renderable r : getSystems(Renderable.class)) {
            r.render(this, batch);
        }
    }

    public <T> Array<T> getSystems(Class<T> c) {
        Array<T> systems = new Array<T>();

        for (EntitySystem system : this.systems) {
            if (c.isInstance(system)) {
                systems.add((T) system);
            }
        }

        return systems;
    }

    public void sendEvent(EntityEvent e) {
        e.sendEvent(this);
    }

    public void addEntity(Entity e) {
        entities.add(e);
        sendEvent(new EntityAddedEvent(e));
    }
}
