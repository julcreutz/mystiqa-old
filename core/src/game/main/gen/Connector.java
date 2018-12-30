package game.main.gen;

import com.badlogic.gdx.utils.JsonValue;
import game.Range;
import game.loader.Serializable;
import game.main.Game;
import game.main.gen.WorldGenerator;
import game.main.state.play.tile.TileType;

public class Connector implements Serializable {
    public TileType tile;

    public Range absoluteDiffX;
    public Range absoluteDiffY;

    public Range wayThickness;

    public boolean fits(Connection c) {
        return absoluteDiffX.inRange(c.absoluteDiffX) && absoluteDiffY.inRange(c.absoluteDiffY) && wayThickness.inRange(c.wayThickness);
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("tile")) {
            tile = Game.TILES.load(json.getString("tile"));
        }

        absoluteDiffX = new Range();
        if (json.has("absoluteDiffX")) {
            absoluteDiffX.deserialize(json.get("absoluteDiffX"));
        }

        absoluteDiffY = new Range();
        if (json.has("absoluteDiffY")) {
            absoluteDiffY.deserialize(json.get("absoluteDiffY"));
        }

        wayThickness = new Range();
        if (json.has("wayThickness")) {
            wayThickness.deserialize(json.get("wayThickness"));
        }
    }
}
