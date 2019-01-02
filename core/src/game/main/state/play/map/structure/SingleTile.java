package game.main.state.play.map.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;
import game.main.state.play.map.Map;

import java.util.Random;

public class SingleTile extends Structure<Map> {
    public String tile;

    @Override
    public void generate(Random rand, Map map, int x, int y, int z) {
        map.tiles.set(Game.TILES.load(tile), x, y, z);
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue tile = json.get("tile");
        if (tile != null) {
            this.tile = tile.asString();
        }
    }

    @Override
    public String tile() {
        return tile;
    }
}
