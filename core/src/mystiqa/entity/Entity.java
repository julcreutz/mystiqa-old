package mystiqa.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.world.Chunk;

public class Entity {
    public float x;
    public float y;
    public float z;

    public Hitbox hitbox;

    public Entity() {
        hitbox = new Hitbox();
    }

    public void update() {

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

    public int getChunkX() {
        return MathUtils.floor(getTileX() / (float) Chunk.WIDTH) * Chunk.WIDTH;
    }

    public int getChunkY() {
        return MathUtils.floor(getTileY() / (float) Chunk.HEIGHT) * Chunk.HEIGHT;
    }

    public int getChunkZ() {
        return MathUtils.floor(getTileZ() / (float) Chunk.DEPTH) * Chunk.DEPTH;
    }

    public void onAdded() {

    }

    public void deserialize(JsonValue json) {

    }
}
