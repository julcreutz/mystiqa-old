package game.main.state.play.map.world;

import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;
import game.main.state.play.map.tile.Tile;
import game.range.IntRange;
import game.loader.Serializable;

public class Connector implements Serializable {
    public String replace;
    public String tile;

    public float chance;

    public IntRange absoluteDiffX;
    public IntRange absoluteDiffY;

    public IntRange wayThickness;

    public Connector() {
        chance = 1;
    }

    public boolean fits(Connection c, Tile t) {
        return (replace == null || t.name.equals(replace))
                && Game.RANDOM.nextFloat() < chance
                && (absoluteDiffX == null || absoluteDiffX.inRange(c.absoluteDiffX))
                && (absoluteDiffY == null || absoluteDiffY.inRange(c.absoluteDiffY))
                && (wayThickness == null || wayThickness.inRange(c.wayThickness));
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue replace = json.get("replace");
        if (replace != null) {
            this.replace = replace.asString();
        }

        JsonValue tile = json.get("tile");
        if (tile != null) {
            this.tile = tile.asString();
        }

        JsonValue chance = json.get("chance");
        if (chance != null) {
            this.chance = chance.asFloat();
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
