package mystiqa.item.equipable.armor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mystiqa.item.equipable.Equipable;

public abstract class Armor extends Equipable {
    public TextureRegion[][] graphics;

    public void render(SpriteBatch batch, float x, float y, int step, int dir, boolean flipped) {
        batch.setColor(material.color);

        TextureRegion t = graphics[step][dir];
        batch.draw(t, x, y, t.getRegionWidth() * .5f, t.getRegionHeight() * .5f, t.getRegionWidth(), t.getRegionHeight(), flipped ? -1 : 1, 1, 0);
    }

    public void render(SpriteBatch batch, float x, float y, int step, int dir) {
        render(batch, x, y, step, dir, false);
    }
}
