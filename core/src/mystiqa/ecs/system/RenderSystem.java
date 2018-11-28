package mystiqa.ecs.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mystiqa.ecs.EntityManager;
import mystiqa.ecs.component.PositionComponent;
import mystiqa.ecs.component.RenderComponent;
import mystiqa.ecs.entity.Entity;

public class RenderSystem implements EntitySystem, Renderable {
    @Override
    public void render(EntityManager em, SpriteBatch batch) {
        for (Entity e : em.getEntities(PositionComponent.class, RenderComponent.class)) {
            PositionComponent pos = e.getComponent(PositionComponent.class);
            RenderComponent render = e.getComponent(RenderComponent.class);

            batch.draw(render.img, pos.x, pos.y + pos.z);
        }
    }
}
