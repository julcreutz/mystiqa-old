package mystiqa.entity.tile;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;

public class TileType {
    public String name;

    public TextureRegion[][] topGraphics;
    public TextureRegion[][] sideGraphics;

    public Color topColor;
    public Color sideColor;

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
