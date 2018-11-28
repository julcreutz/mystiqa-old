package mystiqa.entity.tile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Assets;

public class TileType {
    public String name;

    public TextureRegion[][] topGraphics;
    public TextureRegion[][] sideGraphics;

    public Color topColor;
    public Color sideColor;

    public boolean solid;

    public TileType() {
        solid = true;
    }

    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("topGraphics")) {
            topGraphics = Assets.getSpriteSheet(json.getString("topGraphics"));
        }

        if (json.has("sideGraphics")) {
            sideGraphics = Assets.getSpriteSheet(json.getString("sideGraphics"));
        }

        if (json.has("solid")) {
            solid = json.getBoolean("solid");
        }

        if (json.has("topColor")) {
            topColor = Assets.getColor(json.getString("topColor"));
        }

        if (json.has("sideColor")) {
            sideColor = Assets.getColor(json.getString("sideColor"));
        }
    }
}
