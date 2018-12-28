package game.loader;

import game.main.play.entity.Entity;
import game.main.play.entity.humanoid.Humanoid;
import game.main.play.entity.slime.Slime;

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
