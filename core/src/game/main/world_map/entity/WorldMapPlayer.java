package game.main.world_map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import game.main.Game;

public class WorldMapPlayer extends WorldMapEntity {
    public WorldMapPlayerType type;

    public int dir;
    public float time;

    public WorldMapPlayer(WorldMapPlayerType type, float x, float y) {
        super(x, y);
        this.type = type;
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        int step = MathUtils.floor(time * type.animSpeed);

        float y = this.y + (step % 2 != 0 ? -1 : 0);

        batch.setColor(type.color);

        batch.draw(type.feet[(step + 2) % type.feet.length][dir], x, y);
        batch.draw(type.body[1 + step % (type.body.length - 1)][dir], x, y);

        batch.draw(type.body[0][dir], x, y);

        batch.draw(type.feet[step % type.feet.length][dir], x, y);

        batch.draw(type.head[step % type.head.length][dir], x, y);

        batch.draw(type.body[1 + (step + 2) % (type.body.length - 1)][dir], x, y);

        batch.setColor(1, 1, 1, 1);
    }
}
