package mystiqa.ecs.system;

import mystiqa.ecs.EntityManager;

public interface Updateable {
    void update(EntityManager em);
}
