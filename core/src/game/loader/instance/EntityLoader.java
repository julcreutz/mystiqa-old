package game.loader.instance;

import game.main.state.play.map.entity.*;

public class EntityLoader extends InstanceLoader<Entity> {
    @Override
    public Entity newInstance(String name) {
        if (name.equals("Humanoid")) {
            return new Humanoid();
        } else if (name.equals("Slime")) {
            return new Slime();
        } else if (name.equals("Door")) {
            return new Door();
        } else if (name.equals("Block")) {
            return new Block();
        }

        return null;
    }
}
