package game.main.play.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.loader.SheetLoader;
import game.loader.palette.PaletteShaderLoader;

public class Slime extends Entity {
    public Slime() {
        hitbox.set(8, 8, 0, 0);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.setShader(PaletteShaderLoader.load(new String[] {"Black", "Green"}));
        batch.draw(SheetLoader.load("Slime")[0][0], x, y);
    }
}
