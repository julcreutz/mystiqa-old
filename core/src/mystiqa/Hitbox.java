package mystiqa;

import com.badlogic.gdx.math.Rectangle;
import mystiqa.entity.Entity;

public class Hitbox {
    public Rectangle hitbox;

    public float x;
    public float y;

    public Hitbox() {
        hitbox = new Rectangle();
    }

    public void set(float x, float y, int w, int h) {
        this.x = x;
        this.y = y;

        hitbox.setSize(w, h);
    }

    public void update(Entity e, float x, float y) {
        hitbox.setPosition(e.x + this.x + x, e.y + this.y + y);
    }

    public void update(Entity e) {
        update(e, 0, 0);
    }

    public boolean overlaps(Hitbox hitbox) {
        return this.hitbox.width > 0 && this.hitbox.height > 0 && this.hitbox.overlaps(hitbox.hitbox);
    }
}
