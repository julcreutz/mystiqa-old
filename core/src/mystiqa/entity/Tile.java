package mystiqa.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;
import mystiqa.main.screen.PlayScreen;

public class Tile extends Entity {
    public String name;

    public TextureRegion[][] topGraphics;
    public TextureRegion topGraphic;

    public TextureRegion[][] sideGraphics;
    public TextureRegion sideGraphic;

    public Tile() {
        hitbox.set(0, 0, 0, 16, 16, 16);
    }

    @Override
    public void update(PlayScreen play) {
        super.update(play);

        int x = (int) (this.x / 16f);
        int y = (int) (this.y / 16f);
        int z = (int) (this.z / 16f);

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
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (sideGraphics != null) {
            batch.draw(sideGraphics[3][3], x, y + z);
        }

        if (topGraphic != null) {
            batch.draw(topGraphic, x, y + z + 16);
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
    }
}
