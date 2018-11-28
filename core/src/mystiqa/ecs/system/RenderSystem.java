package mystiqa.ecs.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mystiqa.ecs.EntityManager;
import mystiqa.ecs.component.ColorComponent;
import mystiqa.ecs.component.PositionComponent;
import mystiqa.ecs.component.RenderComponent;
import mystiqa.ecs.entity.Entity;

@RequireComponent({PositionComponent.class, RenderComponent.class})
public class RenderSystem implements EntitySystem, Renderable {
    @Override
    public void render(EntityManager em, SpriteBatch batch) {
        for (Entity e : em.getEntities(getClass())) {
            PositionComponent pos = e.getComponent(PositionComponent.class);
            RenderComponent render = e.getComponent(RenderComponent.class);

            ColorComponent color = e.getComponent(ColorComponent.class);
            if (color != null) {
                batch.setColor(color.r, color.g, color.b, 1);
            }

            batch.draw(render.img, pos.x, pos.y + pos.z);

            batch.setColor(1, 1, 1, 1);
        }
    }
}
