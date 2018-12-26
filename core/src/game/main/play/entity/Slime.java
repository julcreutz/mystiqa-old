package game.main.play.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.loader.SheetLoader;

public class Slime extends Entity {
    public Slime() {
        hitbox.set(8, 8, 0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);
        batch.draw(SheetLoader.load("Slime")[0][0], x, y);
    }

    @Override
    public String[] colors() {
        return new String[] {"Black", "Green"};
    }
}
