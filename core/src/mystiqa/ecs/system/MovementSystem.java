package mystiqa.ecs.system;

import mystiqa.ecs.EntityManager;
import mystiqa.ecs.component.PositionComponent;
import mystiqa.ecs.component.VelocityComponent;
import mystiqa.ecs.entity.Entity;
import mystiqa.ecs.event.MoveEvent;
import mystiqa.main.Game;

public class MovementSystem implements EntitySystem, Updateable {
    @Override
    public void update(EntityManager em) {
        for (Entity e : em.getEntities(PositionComponent.class, VelocityComponent.class)) {
            PositionComponent pos = e.getComponent(PositionComponent.class);
            VelocityComponent vel = e.getComponent(VelocityComponent.class);

            pos.x += vel.x * Game.getDelta();
            pos.y += vel.y * Game.getDelta();
            pos.z += vel.z * Game.getDelta();

            em.sendEvent(new MoveEvent(e, vel.x, vel.y, vel.z));

            vel.x = 0;
            vel.y = 0;
        }
    }
}
