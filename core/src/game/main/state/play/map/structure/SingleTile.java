package game.main.state.play.map.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;
import game.main.state.play.Play;
import game.main.state.play.map.Map;
import game.main.state.play.map.tile.TileType;

import java.util.Random;

public class SingleTile extends Structure {
    public TileType tile;

    @Override
    public void generate(Random rand, Map map, int x, int y, int z) {
        map.tiles.placeTile(tile, x, y, z);
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("tile")) {
            tile = Game.TILES.load(json.getString("tile"));
        }
    }

    @Override
    public TileType getTile() {
        return tile;
    }
}
