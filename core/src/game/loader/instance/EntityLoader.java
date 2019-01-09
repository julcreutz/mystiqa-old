package game.loader.instance;

import game.main.state.play.map.entity.Door;
import game.main.state.play.map.entity.Entity;
import game.main.state.play.map.entity.Humanoid;
import game.main.state.play.map.entity.Slime;

public class EntityLoader extends InstanceLoader<Entity> {
    @Override
    public Entity newInstance(String name) {
        if (name.equals("Humanoid")) {
            return new Humanoid();
        } else if (name.equals("Slime")) {
            return new Slime();
        } else if (name.equals("Door")) {
            return new Door();
        }

        return null;
    }
}
