package mystiqa.ecs.system;

import mystiqa.Assets;
import mystiqa.ecs.EntityManager;
import mystiqa.ecs.component.RenderComponent;
import mystiqa.ecs.entity.Entity;

public class AnimationSystem implements EntitySystem, Updateable {
    @Override
    public void update(EntityManager em) {
        for (Entity e : em.getEntities(RenderComponent.class)) {
            e.getComponent(RenderComponent.class).img = Assets.getInstance().getSpriteSheet("HumanBody")[0][0];
        }
    }
}
