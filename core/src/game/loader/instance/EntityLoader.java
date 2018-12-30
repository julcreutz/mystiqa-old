package game.loader.instance;

import game.main.state.play.entity.Entity;
import game.main.state.play.entity.Humanoid;
import game.main.state.play.entity.Slime;

public class EntityLoader extends InstanceLoader<Entity> {
    @Override
    public Entity newInstance(String name) {
        Entity e = null;

        if (name.equals("Humanoid")) {
            e = new Humanoid();
        } else if (name.equals("Slime")) {
            e = new Slime();
        }

        return e;
    }
}
