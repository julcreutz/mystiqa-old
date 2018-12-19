package game.main.play.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.TileLoader;
import game.main.play.Play;
import game.main.play.tile.TileType;

import java.util.Random;

public class SingleTile extends Structure {
    public TileType tile;

    @Override
    public void generate(Random rand, Play play, int x, int y, int z) {
        super.generate(rand, play, x, y, z);

        play.placeTile(tile, x, y, z);
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("tile")) {
            tile = TileLoader.load(json.getString("tile"));
        }
    }
}
