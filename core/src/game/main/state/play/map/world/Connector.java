package game.main.state.play.map.world;

import com.badlogic.gdx.utils.JsonValue;
import game.Range;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.map.tile.Tile;

public class Connector implements Serializable {
    public String tile;

    public Range absoluteDiffX;
    public Range absoluteDiffY;

    public Range wayThickness;

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
            this.absoluteDiffX = new Range(absoluteDiffX);
        }

        JsonValue absoluteDiffY = json.get("absoluteDiffY");
        if (absoluteDiffY != null) {
            this.absoluteDiffY = new Range(absoluteDiffY);
        }

        JsonValue wayThickness = json.get("wayThickness");
        if (wayThickness != null) {
            this.wayThickness = new Range(wayThickness);
        }
    }
}
