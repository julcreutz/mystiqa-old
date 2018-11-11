package mystiqa.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mystiqa.Hitbox;
import mystiqa.main.screen.PlayScreen;

public class Entity {
    public float x;
    public float y;
    public float z;

    public Hitbox hitbox;

    public Entity() {
        hitbox = new Hitbox();
    }

    public void update(PlayScreen play) {

    }

    public void render(SpriteBatch batch) {

    }

    public int getTileX() {
        return (int) (x / 8);
    }

    public int getTileY() {
        return (int) (y / 8);
    }

    public int getTileZ() {
        return (int) (z / 8);
    }
}
