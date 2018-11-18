package mystiqa.entity.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;

public class TileType {
    public String name;

    public TextureRegion[][] topGraphics;
    public TextureRegion[][] sideGraphics;

    public boolean solid;

    public TileType() {
        solid = true;
    }

    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("topGraphics")) {
            topGraphics = Resources.getInstance().getSpriteSheet(json.getString("topGraphics"));
        }

        if (json.has("sideGraphics")) {
            sideGraphics = Resources.getInstance().getSpriteSheet(json.getString("sideGraphics"));
        }

        if (json.has("solid")) {
            solid = json.getBoolean("solid");
        }
    }
}
