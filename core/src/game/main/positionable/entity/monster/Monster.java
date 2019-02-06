package game.main.positionable.entity.monster;

import game.main.positionable.entity.Entity;

public class Monster extends Entity {
    @Override
    public boolean isHostile(Entity e) {
        return e == map.player;
    }
}
