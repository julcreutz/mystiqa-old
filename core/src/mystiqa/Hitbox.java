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

    public void set(int x, int y, int w, int h) {
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
}
