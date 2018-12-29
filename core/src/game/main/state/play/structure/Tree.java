package game.main.state.play.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;
import game.main.state.play.Play;
import game.main.state.play.tile.TileType;

import java.util.Random;

public class Tree extends Structure {
    public TileType bottomTile;
    public TileType middleTile;
    public TileType topTile;

    public int minHeight;
    public int maxHeight;

    @Override
    public void generate(Random rand, Play play, int x, int y, int z) {
        play.placeTile(bottomTile, x, y, z);

        z++;
        for (int i = 0; i < minHeight + rand.nextInt(maxHeight - minHeight + 1); i++) {
            play.placeTile(middleTile, x, y, z);
            z++;
        }

        play.placeTile(topTile, x, y, z);
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

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
    public TileType getTile() {
        return bottomTile;
    }
}