package game.main.positionable.entity.monster;

import com.badlogic.gdx.math.MathUtils;
import game.main.positionable.entity.Entity;

public class Monster extends Entity {
    public float experience;
    public float experiencePerLevel;

    public float getExperience() {
        return experience + MathUtils.floor(level * experiencePerLevel);
    }

    @Override
    public boolean isHostile(Entity e) {
        return e == map.player;
    }

    @Override
    public void onDeath() {
        super.onDeath();

        map.player.addExperience(getExperience());
    }
}
