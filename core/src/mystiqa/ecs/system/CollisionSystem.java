package mystiqa.ecs.system;

import com.badlogic.gdx.utils.Array;
import mystiqa.ecs.EntityManager;
import mystiqa.ecs.component.CollisionComponent;
import mystiqa.ecs.component.PositionComponent;
import mystiqa.ecs.component.VelocityComponent;
import mystiqa.ecs.entity.Entity;
import mystiqa.ecs.event.CollisionEvent;
import mystiqa.ecs.event.GroundCollisionEvent;
import mystiqa.main.Game;

public class CollisionSystem implements EntitySystem, Updateable {
    @Override
    public void update(EntityManager em) {
        Array<Entity> entities = em.getEntities(PositionComponent.class, CollisionComponent.class);

        for (int i = 0; i < entities.size; i++) {
            for (int j = 0; j < entities.size; j++) {
                if (i != j) {
                    Entity e = entities.get(i);
                    Entity _e = entities.get(j);

                    PositionComponent pos = e.getComponent(PositionComponent.class);
                    CollisionComponent collision = e.getComponent(CollisionComponent.class);

                    PositionComponent _pos = _e.getComponent(PositionComponent.class);
                    CollisionComponent _collision = _e.getComponent(CollisionComponent.class);

                    VelocityComponent vel = e.getComponent(VelocityComponent.class);

                    if (vel != null) {
                        boolean collided = false;

                        if (collision.overlaps(pos.x + vel.x * Game.getDelta(), pos.y, pos.z, _collision, _pos.x, _pos.y, _pos.z)) {
                            if (vel.x > 0) {
                                pos.x = _pos.x + _collision.x - collision.width - collision.x;
                            } else if (vel.x < 0) {
                                pos.x = _pos.x + _collision.x + _collision.width - collision.x;
                            }

                            vel.x = 0;

                            collided = true;
                            em.sendEvent(new CollisionEvent(e, _e));
                        }

                        if (collision.overlaps(pos.x, pos.y + vel.y * Game.getDelta(), pos.z, _collision, _pos.x, _pos.y, _pos.z)) {
                            if (vel.y > 0) {
                                pos.y = _pos.y + _collision.y - collision.height - collision.y;
                            } else if (vel.y < 0) {
                                pos.y = _pos.y + _collision.y + _collision.height - collision.y;
                            }

                            vel.y = 0;

                            if (!collided) {
                                collided = true;
                                em.sendEvent(new CollisionEvent(e, _e));
                            }
                        }

                        if (collision.overlaps(pos.x, pos.y, pos.z + vel.z * Game.getDelta(), _collision, _pos.x, _pos.y, _pos.z)) {
                            if (vel.z > 0) {
                                pos.z = _pos.z + _collision.z - collision.depth - collision.z;
                            } else if (vel.z < 0) {
                                pos.z = _pos.z + _collision.z + _collision.depth - collision.z;
                                em.sendEvent(new GroundCollisionEvent(e));
                            }

                            vel.z = 0;

                            if (!collided) {
                                em.sendEvent(new CollisionEvent(e, _e));
                            }
                        }
                    }
                }
            }
        }
    }
}
