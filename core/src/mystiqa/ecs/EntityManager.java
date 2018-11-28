package mystiqa.ecs;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import mystiqa.ecs.component.EntityComponent;
import mystiqa.ecs.entity.Entity;
import mystiqa.ecs.event.EntityAddedEvent;
import mystiqa.ecs.event.EntityEvent;
import mystiqa.ecs.event.EntityListener;
import mystiqa.ecs.system.EntitySystem;
import mystiqa.ecs.system.Renderable;
import mystiqa.ecs.system.RequireComponent;
import mystiqa.ecs.system.Updateable;

import java.lang.annotation.Annotation;

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

    public <T extends EntitySystem> Array<Entity> getEntities(Class<T> c) {
        Array<Entity> entities = new Array<Entity>();

        for (Entity e : this.entities) {
            if (isEntitySuitable(e, getRequiredComponents(c))) {
                entities.add(e);
            }
        }

        return entities;
    }

    public Class<? extends EntityComponent>[] getRequiredComponents(Class<?> c) {
        RequireComponent a = c.getAnnotation(RequireComponent.class);

        return a != null ? a.value() : new Class[] {};
    }

    public boolean isEntitySuitable(Entity e, Class<? extends EntityComponent>... c) {
        boolean b;

        for (Class<? extends EntityComponent> _c : c) {
            b = false;

            for (EntityComponent component : e.components) {
                if (_c.isInstance(component)) {
                    b = true;
                }
            }

            if (!b) {
                return false;
            }
        }

        return true;
    }

    public void sendEvent(EntityEvent e) {
        e.sendEvent(this);
    }

    public void addEntity(Entity e) {
        entities.add(e);
        sendEvent(new EntityAddedEvent(e));
    }

    public void addSystem(EntitySystem s) {
        systems.add(s);
    }
}
