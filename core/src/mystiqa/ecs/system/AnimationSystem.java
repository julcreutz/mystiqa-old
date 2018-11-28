package mystiqa.ecs.system;

import mystiqa.Assets;
import mystiqa.ecs.EntityManager;
import mystiqa.ecs.component.RenderComponent;
import mystiqa.ecs.entity.Entity;

@RequireComponent(RenderComponent.class)
public class AnimationSystem implements EntitySystem, Updateable {
    @Override
    public void update(EntityManager em) {
        for (Entity e : em.getEntities(getClass())) {
            e.getComponent(RenderComponent.class).img = Assets.getInstance().getSpriteSheet("HumanBody")[0][0];
        }
    }
}
