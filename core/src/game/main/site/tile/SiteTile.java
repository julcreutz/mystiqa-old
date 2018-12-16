package game.main.site.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.loader.palette.PaletteShaderLoader;
import game.main.site.SiteData;
import game.main.site.SiteState;

public class SiteTile {
    public SiteTileType type;

    public TextureRegion sideImage;
    public TextureRegion topImage;

    public int x;
    public int y;
    public int z;

    public void update(SiteData site) {
        if (y <= 0 || site.tiles[x][y - 1][z] == null) {
            boolean hl = connect(site, x - 1, y, z);
            boolean hr = connect(site, x + 1, y, z);

            int tx = hl && hr ? 1 : (hl ? 2 : (hr ? 0 : 3));

            boolean hb = connect(site, x, y, z - 1);
            boolean ht = connect(site, x, y, z + 1);

            int ty = hb && ht ? 1 : (hb ? 0 : (ht ? 2 : 3));

            sideImage = type.sideSheet[tx][ty];
        } else {
            sideImage = null;
        }

        if (z + 1 < site.tiles[0][0].length && site.tiles[x][y][z + 1] == null) {
            int n = 0;

            if (connect(site, x + 1, y, z)) {
                n++;
            }

            if (connect(site, x, y + 1, z)) {
                n += 2;
            }

            if (connect(site, x - 1, y, z)) {
                n += 4;
            }

            if (connect(site, x, y - 1, z)) {
                n += 8;
            }

            switch (n) {
                case 0:
                    topImage = type.topSheet[3][3];
                    break;
                case 1:
                    topImage = type.topSheet[0][3];
                    break;
                case 2:
                    topImage = type.topSheet[3][2];
                    break;
                case 3:
                    topImage = type.topSheet[0][2];
                    break;
                case 4:
                    topImage = type.topSheet[2][3];
                    break;
                case 5:
                    topImage = type.topSheet[1][3];
                    break;
                case 6:
                    topImage = type.topSheet[2][2];
                    break;
                case 7:
                    topImage = type.topSheet[1][2];
                    break;
                case 8:
                    topImage = type.topSheet[3][0];
                    break;
                case 9:
                    topImage = type.topSheet[0][0];
                    break;
                case 10:
                    topImage = type.topSheet[3][1];
                    break;
                case 11:
                    topImage = type.topSheet[0][1];
                    break;
                case 12:
                    topImage = type.topSheet[2][0];
                    break;
                case 13:
                    topImage = type.topSheet[1][0];
                    break;
                case 14:
                    topImage = type.topSheet[2][1];
                    break;
                case 15:
                    topImage = type.topSheet[1][1];
                    break;
            }
        } else {
            topImage = null;
        }
    }

    public void render(SpriteBatch batch) {
        if (sideImage != null) {
            batch.setShader(PaletteShaderLoader.load(type.sideColors));
            batch.draw(sideImage, x * 8, y * 8 + z * 8);
        }

        if (topImage != null) {
            batch.setShader(PaletteShaderLoader.load(type.topColors));
            batch.draw(topImage, x * 8, y * 8 + z * 8 + 8);
        }

        batch.setColor(1, 1, 1, 1);
        batch.setShader(null);
    }

    public boolean connect(SiteData site, int x, int y, int z) {
        SiteTile tile = site.tileAt(x, y, z);
        return tile != null && type.connect(tile.type);
    }
}
