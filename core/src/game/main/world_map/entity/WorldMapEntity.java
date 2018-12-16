package game.main.world_map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.main.world_map.WorldMapState;

public class WorldMapEntity {
    public float x;
    public float y;

    public WorldMapEntity(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public void update(WorldMapState map) {
    }

    public void render(SpriteBatch batch) {
    }
}
