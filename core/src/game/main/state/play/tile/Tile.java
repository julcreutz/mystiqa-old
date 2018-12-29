package game.main.state.play.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.loader.palette.PaletteShaderLoader;
import game.main.Game;
import game.main.state.play.Play;

public class Tile {
    public TileType type;

    public TextureRegion image;

    public int x;
    public int y;
    public int z;

    public boolean updated;

    public Tile(TileType type) {
        this.type = type;
    }

    public void update(Play site) {
        if (type.autoTile) {
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
                    image = type.sheet[3][3];
                    break;
                case 1:
                    image = type.sheet[0][3];
                    break;
                case 2:
                    image = type.sheet[3][2];
                    break;
                case 3:
                    image = type.sheet[0][2];
                    break;
                case 4:
                    image = type.sheet[2][3];
                    break;
                case 5:
                    image = type.sheet[1][3];
                    break;
                case 6:
                    image = type.sheet[2][2];
                    break;
                case 7:
                    image = type.sheet[1][2];
                    break;
                case 8:
                    image = type.sheet[3][0];
                    break;
                case 9:
                    image = type.sheet[0][0];
                    break;
                case 10:
                    image = type.sheet[3][1];
                    break;
                case 11:
                    image = type.sheet[0][1];
                    break;
                case 12:
                    image = type.sheet[2][0];
                    break;
                case 13:
                    image = type.sheet[1][0];
                    break;
                case 14:
                    image = type.sheet[2][1];
                    break;
                case 15:
                    image = type.sheet[1][1];
                    break;
            }
        } else {
            image = type.sheet[0][0];
        }
    }

    public void render(SpriteBatch batch) {
        batch.setShader(type.palettes.get((int) ((Game.time * type.paletteSpeed) % type.palettes.size)));
        batch.draw(image, x * 8, y * 8 + z * 8);
        batch.setColor(1, 1, 1, 1);
        batch.setShader(null);
    }

    public boolean connect(Play site, int x, int y, int z) {
        Tile tile = site.tileAt(x, y, z);
        return tile != null && type.connectsTo(tile.type);
    }
}
