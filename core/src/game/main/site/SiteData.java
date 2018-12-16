package game.main.site;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import game.main.site.entity.SiteEntity;
import game.main.site.tile.SiteTile;

public class SiteData {
    public SiteTile[][][] tiles;
    public Array<SiteEntity> entities;

    public SiteData() {
        tiles = new SiteTile[16][9][4];
        entities = new Array<SiteEntity>();
    }

    public void update(SiteState state) {
        for (int z = 0; z < tiles[0][0].length; z++) {
            for (int x = 0; x < tiles.length; x++) {
                for (int y = 0; y < tiles[0].length; y++) {
                    SiteTile tile = tiles[x][y][z];

                    if (tile != null) {
                        tile.update(this);
                    }
                }
            }
        }

        for (SiteEntity entity : entities) {
            entity.update(this);
        }
    }

    public void render(SpriteBatch batch) {
        for (int z = 0; z < tiles[0][0].length; z++) {
            for (int x = 0; x < tiles.length; x++) {
                for (int y = tiles[0].length - 1; y >= 0; y--) {
                    SiteTile tile = tiles[x][y][z];

                    if (tile != null) {
                        tile.render(batch);
                    }
                }
            }
        }

        for (SiteEntity entity : entities) {
            entity.render(batch);
        }
    }

    public SiteTile tileAt(int x, int y, int z) {
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0][0].length ? tiles[x][y][z] : null;
    }
}
