package mystiqa.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
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
        return MathUtils.floor(x / 8f);
    }

    public int getTileY() {
        return MathUtils.floor(y / 8f);
    }

    public int getTileZ() {
        return MathUtils.floor(z / 8f);
    }

    public void deserialize(JsonValue json) {

    }
}
