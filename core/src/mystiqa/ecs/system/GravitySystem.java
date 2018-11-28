package mystiqa.ecs.system;

import mystiqa.ecs.EntityManager;
import mystiqa.ecs.component.VelocityComponent;
import mystiqa.ecs.entity.Entity;
import mystiqa.main.Game;

@RequireComponent(VelocityComponent.class)
public class GravitySystem implements EntitySystem, Updateable {
    @Override
    public void update(EntityManager em) {
        for (Entity e : em.getEntities(getClass())) {
            VelocityComponent vel = e.getComponent(VelocityComponent.class);
            vel.z -= 400f * Game.getDelta();
        }
    }
}
