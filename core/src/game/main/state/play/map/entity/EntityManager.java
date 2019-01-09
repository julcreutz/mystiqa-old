package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import game.main.state.play.map.Map;
import game.main.state.play.map.entity.Entity;

import java.util.Comparator;
import java.util.Iterator;

public class EntityManager {
    public Map map;

    public Array<Entity> entities;
    public Array<Entity> invisibleEntities;

    public EntityManager(Map map) {
        this.map = map;

        entities = new Array<Entity>();
        invisibleEntities = new Array<Entity>();
    }

    public void update() {
        for (int i = invisibleEntities.size - 1; i >= 0; i--) {
            Entity e = invisibleEntities.get(i);

            if (map.isVisible(e)) {
                entities.add(e);
                invisibleEntities.removeIndex(i);
            }
        }

        for (int i = entities.size - 1; i >= 0; i--) {
            Entity e = entities.get(i);

            if (!map.isVisible(e)) {
                invisibleEntities.add(e);
                entities.removeIndex(i);
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

        invisibleEntities.add(e);
        e.onAdded();
    }

    public void clear() {
        entities.clear();
        invisibleEntities.clear();
    }
}
