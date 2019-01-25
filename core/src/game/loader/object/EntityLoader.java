package game.loader.object;

import game.main.object.entity.*;
import game.main.object.entity.particle.Particle;

public class EntityLoader extends ObjectLoader<Entity> {
    @Override
    public Entity newInstance(String name) {
        if (name.equals("Humanoid")) {
            return new Humanoid();
        } else if (name.equals("Slime")) {
            return new Slime();
        } else if (name.equals("Dragon")) {
            return new Dragon();
        } else if (name.equals("Particle")) {
            return new Particle();
        } else if (name.equals("Projectile")) {
            return new Projectile();
        } else if (name.equals("Door")) {
            return new Door();
        } else if (name.equals("Block")) {
            return new Block();
        }

        return null;
    }
}
