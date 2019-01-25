package game.main.object.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.main.Game;
import game.main.stat.Stat;

public class Chest extends Entity {
    public Chest() {
        stats.stats.add(new Stat(Stat.Type.HEALTH, 1, 0));
        hitbox.set(8, 4, 0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(Game.SPRITE_SHEETS.load("Chest").grab(0, 0), x, y);
    }
}
