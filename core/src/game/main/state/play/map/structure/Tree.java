package game.main.state.play.map.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;
import game.main.state.play.map.Map;
import game.main.state.play.map.tile.Tile;

import java.util.Random;

public class Tree extends Structure {
    public Tile.Type bottomTile;
    public Tile.Type middleTile;
    public Tile.Type topTile;

    public int minHeight;
    public int maxHeight;

    @Override
    public void generate(Random rand, Map map, int x, int y, int z) {
        map.tiles.placeTile(bottomTile, x, y, z);

        z++;
        for (int i = 0; i < minHeight + rand.nextInt(maxHeight - minHeight + 1); i++) {
            map.tiles.placeTile(middleTile, x, y, z);
            z++;
        }

        map.tiles.placeTile(topTile, x, y, z);
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("bottomTile")) {
            bottomTile = Game.TILES.load(json.getString("bottomTile"));
        }

        if (json.has("middleTile")) {
            middleTile = Game.TILES.load(json.getString("middleTile"));
        }

        if (json.has("topTile")) {
            topTile = Game.TILES.load(json.getString("topTile"));
        }

        minHeight = json.getInt("minHeight", 0);
        maxHeight = json.getInt("maxHeight", 1);
    }

    @Override
    public Tile.Type tile() {
        return bottomTile;
    }
}
