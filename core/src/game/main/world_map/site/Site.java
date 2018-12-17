package game.main.world_map.site;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import game.main.world_map.WorldMapState;

public class Site {
    public SiteType type;

    public int x;
    public int y;

    public Site(SiteType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void update(WorldMapState map) {

    }

    public void render(SpriteBatch batch) {
        batch.setColor(type.color);
        batch.draw(type.sheet[0][0], x * 8, y * 8);
        batch.setColor(1, 1, 1, 1);
    }
}
