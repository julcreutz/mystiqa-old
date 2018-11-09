package mystiqa;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import mystiqa.entity.Entity;

public class Hitbox {
    public float x;
    public float y;
    public float z;

    public float ox;
    public float oy;
    public float oz;

    public int w;
    public int h;
    public int d;

    public TextureRegion border;

    public Hitbox() {
        border = Resources.getSpriteSheet("Hitbox")[0][0];
    }

    public void set(float ox, float oy, float oz, int w, int h, int d) {
        this.ox = ox;
        this.oy = oy;
        this.oz = oz;

        this.w = w;
        this.h = h;
        this.d = d;
    }

    public void update(Entity e, float ox, float oy, float oz) {
        x = e.x + this.ox + ox;
        y = e.y + this.oy + oy;
        z = e.z + this.oz + oz;
    }

    public void update(Entity e) {
        update(e, 0, 0, 0);
    }

    public boolean overlaps(Hitbox hitbox) {
        return x + w > hitbox.x && x < hitbox.x + hitbox.w
                && y + h > hitbox.y && y < hitbox.y + hitbox.h
                && z + d > hitbox.z && z < hitbox.z + hitbox.d;
    }

    public void render(SpriteBatch batch) {
        // Bottom
        batch.draw(border, x, y + z, w, 1);
        batch.draw(border, x, y + z + h - 1, w, 1);

        batch.draw(border, x, y + z, 1, h);
        batch.draw(border, x + w - 1, y + z, 1, h);

        // Vertical lines
        batch.draw(border, x, y + z, 1, d);
        batch.draw(border, x + w - 1, y + z, 1, d);

        batch.draw(border, x, y + z + h - 1, 1, d);
        batch.draw(border, x + w - 1, y + z + h - 1, 1, d);

        // Top
        batch.draw(border, x, y + z + d - 1, w, 1);
        batch.draw(border, x, y + z + h - 1 + d - 1, w, 1);

        batch.draw(border, x, y + z + d - 1, 1, h);
        batch.draw(border, x + w - 1, y + z + d - 1, 1, h);
    }
}
