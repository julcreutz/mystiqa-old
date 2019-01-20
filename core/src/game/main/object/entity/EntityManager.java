package game.main.object.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import game.main.state.play.map.Map;
import game.main.object.entity.event.EntityEvent;
import game.main.object.entity.event.EntityListener;

import java.util.Comparator;

public class EntityManager {
    public Map map;

    public Array<Entity> entities;
    public Array<Entity> invisibleEntities;

    public Array<EntityListener> listeners;

    public EntityManager(Map map) {
        this.map = map;

        entities = new Array<Entity>();
        invisibleEntities = new Array<Entity>();

        listeners = new Array<EntityListener>();
    }

    public void update() {
        for (int i = invisibleEntities.size - 1; i >= 0; i--) {
            Entity e = invisibleEntities.get(i);

            if (map.isVisible(e)) {
                entities.add(e);
                invisibleEntities.removeIndex(i);

                e.onEnabled();
            }
        }

        for (int i = entities.size - 1; i >= 0; i--) {
            Entity e = entities.get(i);

            if (!map.isVisible(e)) {
                invisibleEntities.add(e);
                entities.removeIndex(i);

                e.onDisabled();

                continue;
            }

            if (!e.updated) {
                e.preUpdate();
                e.update();
                e.postUpdate();
                e.updated = true;
            } else if (!map.isCamMoving()) {
                e.preUpdate();
                e.update();
                e.postUpdate();
            }
        }

        // Sort entities by y to create perspective
        entities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return Float.compare(o2.getSortLevel(), o1.getSortLevel());
            }
        });
    }

    public void render(SpriteBatch batch) {
        for (Entity e : entities) {
            e.preRender(batch);
            e.render(batch);
            e.postRender(batch);
        }
    }

    public void addEntity(Entity e) {
        e.map = map;
        e.entities = this;

        entities.add(e);

        e.onAdded();
    }

    public void clear() {
        entities.clear();
        invisibleEntities.clear();
    }

    public void sendEvent(EntityEvent e) {
        for (int i = 0; i < listeners.size; i++) {
            listeners.get(i).eventReceived(e);
        }
    }

    public void addListener(EntityListener listener) {
        listeners.add(listener);
    }
}
