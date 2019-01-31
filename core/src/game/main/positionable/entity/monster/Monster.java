package game.main.positionable.entity.monster;

import game.main.positionable.entity.Entity;

public class Monster extends Entity {
    public int experience;

    @Override
    public boolean isHostile(Entity e) {
        return e == map.player;
    }

    @Override
    public void onDeath() {
        super.onDeath();

        map.player.addExperience(experience);
    }
}
