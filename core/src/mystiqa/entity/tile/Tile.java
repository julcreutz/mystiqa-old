package mystiqa.entity.tile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;
import mystiqa.entity.Entity;
import mystiqa.main.Game;
import mystiqa.main.screen.PlayScreen;

public class Tile extends Entity {
    public String name;

    public TextureRegion[][] topGraphics;
    public TextureRegion topGraphic;

    public TextureRegion[][] sideGraphics;
    public TextureRegion sideGraphic;

    public Color topColor;
    public Color sideColor;

    public Tile() {
        hitbox.set(0, 0, 0, 8, 8, 8);
    }

    @Override
    public void update(PlayScreen play) {
        super.update(play);

        int x = (int) (this.x / 8f);
        int y = (int) (this.y / 8f);
        int z = (int) (this.z / 8f);

        if (topGraphics != null) {
            int n = 0;

            Tile r = play.getTile(x + 1, y, z);
            Tile u = play.getTile(x, y + 1, z);
            Tile l = play.getTile(x - 1, y, z);
            Tile d = play.getTile(x, y - 1, z);

            if (r != null && r.name.equals(name)) {
                n += 1;
            }

            if (u != null && u.name.equals(name)) {
                n += 2;
            }

            if (l != null && l.name.equals(name)) {
                n += 4;
            }

            if (d != null && d.name.equals(name)) {
                n += 8;
            }

            switch (n) {
                case 0:
                    topGraphic = topGraphics[3][3];
                    break;
                case 1:
                    topGraphic = topGraphics[0][3];
                    break;
                case 2:
                    topGraphic = topGraphics[3][2];
                    break;
                case 3:
                    topGraphic = topGraphics[0][2];
                    break;
                case 4:
                    topGraphic = topGraphics[2][3];
                    break;
                case 5:
                    topGraphic = topGraphics[1][3];
                    break;
                case 6:
                    topGraphic = topGraphics[2][2];
                    break;
                case 7:
                    topGraphic = topGraphics[1][2];
                    break;
                case 8:
                    topGraphic = topGraphics[3][0];
                    break;
                case 9:
                    topGraphic = topGraphics[0][0];
                    break;
                case 10:
                    topGraphic = topGraphics[3][1];
                    break;
                case 11:
                    topGraphic = topGraphics[0][1];
                    break;
                case 12:
                    topGraphic = topGraphics[2][0];
                    break;
                case 13:
                    topGraphic = topGraphics[1][0];
                    break;
                case 14:
                    topGraphic = topGraphics[2][1];
                    break;
                case 15:
                    topGraphic = topGraphics[1][1];
                    break;
            }
        }

        if (sideGraphics != null) {
            Tile l = play.getTile(x - 1, y, z);
            Tile r = play.getTile(x + 1, y, z);

            boolean hl = l != null && l.name.equals(name);
            boolean hr = r != null && r.name.equals(name);

            int tx = hl && hr ? 1 : (hl && !hr ? 2 : (!hl && hr ? 0 : 3));

            Tile b = play.getTile(x, y, z - 1);
            Tile t = play.getTile(x, y, z + 1);

            boolean hb = b != null && b.name.equals(name);
            boolean ht = t != null && t.name.equals(name);

            int ty = hb && ht ? 1 : (hb && !ht ? 0 : (!hb && ht ? 2 : 3));

            sideGraphic = sideGraphics[tx][ty];
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (sideGraphics != null) {
            batch.setShader(Game.colorToRelative(sideColor));
            batch.draw(sideGraphic, x, y + z);
        }

        if (topGraphic != null) {
            batch.setShader(Game.colorToRelative(topColor));
            batch.draw(topGraphic, x, y + z + 8);
        }
    }

    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("topGraphics")) {
            topGraphics = Resources.getSpriteSheet(json.getString("topGraphics"));
        }

        if (json.has("sideGraphics")) {
            sideGraphics = Resources.getSpriteSheet(json.getString("sideGraphics"));
        }

        if (json.has("topColor")) {
            topColor = Resources.getColor(json.getString("topColor"));
        }

        if (json.has("sideColor")) {
            sideColor = Resources.getColor(json.getString("sideColor"));
        }
    }
}
