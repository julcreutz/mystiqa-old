package game.loader.instance;

import game.main.state.play.entity.Entity;
import game.main.state.play.entity.Humanoid;
import game.main.state.play.entity.Slime;

public class EntityLoader extends InstanceLoader<Entity> {
    @Override
    public Entity newInstance(String name) {
        Entity e = null;

        switch (name) {
            case "Humanoid":
                e = new Humanoid();
                break;
            case "Slime":
                e = new Slime();
                break;
        }

        return e;
    }
}
