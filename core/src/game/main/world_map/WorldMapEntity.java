package game.main.world_map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import game.main.Game;

public class WorldMapEntity {
    public WorldMapEntityType type;

    public int dir;

    public float x;
    public float y;

    public WorldMapEntity(WorldMapEntityType type, float x, float y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void update(WorldMap map) {
    }

    public void render(SpriteBatch batch) {
        batch.draw(type.sheet[MathUtils.floor(Game.time * type.animSpeed) % type.sheet.length][dir], x, y);
    }
}
