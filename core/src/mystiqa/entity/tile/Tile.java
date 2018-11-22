package mystiqa.entity.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Assets;
import mystiqa.entity.Entity;
import mystiqa.main.screen.Play;

public class Tile extends Entity {
    public TileType type;

    public TextureRegion topGraphic;
    public TextureRegion sideGraphic;

    public Tile() {
        hitbox.set(0, 0, 0, 8, 8, 8);
    }

    @Override
    public void update() {
        int x = getTileX();
        int y = getTileY();
        int z = getTileZ();

        if (type.topGraphics != null) {
            int n = 0;

            Tile r = Play.getInstance().getTile(x + 1, y, z);
            Tile u = Play.getInstance().getTile(x, y + 1, z);
            Tile l = Play.getInstance().getTile(x - 1, y, z);
            Tile d = Play.getInstance().getTile(x, y - 1, z);

            if (r != null && r.type.name.equals(type.name)) {
                n += 1;
            }

            if (u != null && u.type.name.equals(type.name)) {
                n += 2;
            }

            if (l != null && l.type.name.equals(type.name)) {
                n += 4;
            }

            if (d != null && d.type.name.equals(type.name)) {
                n += 8;
            }

            switch (n) {
                case 0:
                    topGraphic = type.topGraphics[3][3];
                    break;
                case 1:
                    topGraphic = type.topGraphics[0][3];
                    break;
                case 2:
                    topGraphic = type.topGraphics[3][2];
                    break;
                case 3:
                    topGraphic = type.topGraphics[0][2];
                    break;
                case 4:
                    topGraphic = type.topGraphics[2][3];
                    break;
                case 5:
                    topGraphic = type.topGraphics[1][3];
                    break;
                case 6:
                    topGraphic = type.topGraphics[2][2];
                    break;
                case 7:
                    topGraphic = type.topGraphics[1][2];
                    break;
                case 8:
                    topGraphic = type.topGraphics[3][0];
                    break;
                case 9:
                    topGraphic = type.topGraphics[0][0];
                    break;
                case 10:
                    topGraphic = type.topGraphics[3][1];
                    break;
                case 11:
                    topGraphic = type.topGraphics[0][1];
                    break;
                case 12:
                    topGraphic = type.topGraphics[2][0];
                    break;
                case 13:
                    topGraphic = type.topGraphics[1][0];
                    break;
                case 14:
                    topGraphic = type.topGraphics[2][1];
                    break;
                case 15:
                    topGraphic = type.topGraphics[1][1];
                    break;
            }
        }

        if (type.sideGraphics != null) {
            Tile l = Play.getInstance().getTile(x - 1, y, z);
            Tile r = Play.getInstance().getTile(x + 1, y, z);

            boolean hl = l != null && l.type.name.equals(type.name);
            boolean hr = r != null && r.type.name.equals(type.name);

            int tx = hl && hr ? 1 : (hl && !hr ? 2 : (!hl && hr ? 0 : 3));

            Tile b = Play.getInstance().getTile(x, y, z - 1);
            Tile t = Play.getInstance().getTile(x, y, z + 1);

            boolean hb = b != null && b.type.name.equals(type.name);
            boolean ht = t != null && t.type.name.equals(type.name);

            int ty = hb && ht ? 1 : (hb && !ht ? 0 : (!hb && ht ? 2 : 3));

            sideGraphic = type.sideGraphics[tx][ty];
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (type.sideGraphics != null) {
            batch.setColor(type.sideColor);
            batch.draw(sideGraphic, x, y + z);
        }

        if (topGraphic != null) {
            batch.setColor(type.topColor);
            batch.draw(topGraphic, x, y + z + 8);
        }
    }
}
