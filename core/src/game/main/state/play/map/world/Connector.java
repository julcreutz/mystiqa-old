package game.main.state.play.map.world;

import com.badlogic.gdx.utils.JsonValue;
import game.range.IntRange;
import game.loader.Serializable;

public class Connector implements Serializable {
    public String tile;

    public IntRange absoluteDiffX;
    public IntRange absoluteDiffY;

    public IntRange wayThickness;

    public boolean fits(Connection c) {
        return (absoluteDiffX == null || absoluteDiffX.inRange(c.absoluteDiffX)) && (absoluteDiffY == null || absoluteDiffY.inRange(c.absoluteDiffY)) && (wayThickness == null || wayThickness.inRange(c.wayThickness));
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue tile = json.get("tile");
        if (json.has("tile")) {
            this.tile = tile.asString();
        }

        JsonValue absoluteDiffX = json.get("absoluteDiffX");
        if (absoluteDiffX != null) {
            this.absoluteDiffX = new IntRange(absoluteDiffX);
        }

        JsonValue absoluteDiffY = json.get("absoluteDiffY");
        if (absoluteDiffY != null) {
            this.absoluteDiffY = new IntRange(absoluteDiffY);
        }

        JsonValue wayThickness = json.get("wayThickness");
        if (wayThickness != null) {
            this.wayThickness = new IntRange(wayThickness);
        }
    }
}
