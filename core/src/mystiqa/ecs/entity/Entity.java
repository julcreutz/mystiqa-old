package mystiqa.ecs.entity;

import com.badlogic.gdx.utils.Array;
import mystiqa.ecs.component.EntityComponent;

public class Entity {
    public Array<EntityComponent> components;

    public Entity() {
        components = new Array<EntityComponent>();
    }
}
