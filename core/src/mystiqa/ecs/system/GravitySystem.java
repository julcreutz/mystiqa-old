package mystiqa.ecs.system;

import mystiqa.ecs.EntityManager;
import mystiqa.ecs.component.GravityComponent;
import mystiqa.ecs.component.VelocityComponent;
import mystiqa.ecs.entity.Entity;
import mystiqa.ecs.event.GroundCollisionEvent;
import mystiqa.ecs.event.GroundCollisionListener;
import mystiqa.main.Game;

public class GravitySystem implements EntitySystem, Updateable, GroundCollisionListener {
    @Override
    public void update(EntityManager em) {
        for (Entity e : em.getEntities(VelocityComponent.class, GravityComponent.class)) {
            VelocityComponent vel = e.getComponent(VelocityComponent.class);
            GravityComponent grav = e.getComponent(GravityComponent.class);

            vel.z -= 400f * Game.getDelta();
            //grav.vel -= 9.81f * Game.getDelta();
        }
    }

    @Override
    public void onGroundCollision(GroundCollisionEvent e) {
        //e.e.getComponent(GravityComponent.class).vel = 0;
    }
}
