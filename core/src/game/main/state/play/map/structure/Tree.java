package game.main.state.play.map.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.range.IntRange;
import game.main.Game;
import game.main.state.play.map.Map;

import java.util.Random;

public class Tree extends Structure<Map> {
    public String bottomTile;
    public String middleTile;
    public String topTile;

    public IntRange height;

    @Override
    public void generate(Random rand, Map map, int x, int y, int z) {
        map.tiles.set(Game.TILES.load(bottomTile), x, y, z);

        z++;
        for (int i = 0; i < height.pickRandom(rand); i++) {
            map.tiles.set(Game.TILES.load(middleTile), x, y, z);
            z++;
        }

        map.tiles.set(Game.TILES.load(topTile), x, y, z);
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue bottomTile = json.get("bottomTile");
        if (bottomTile != null) {
            this.bottomTile = bottomTile.asString();
        }

        JsonValue middleTile = json.get("middleTile");
        if (middleTile != null) {
            this.middleTile = middleTile.asString();
        }

        JsonValue topTile = json.get("topTile");
        if (topTile != null) {
            this.topTile = topTile.asString();
        }

        JsonValue height = json.get("height");
        if (height != null) {
            this.height = new IntRange(height);
        }
    }

    @Override
    public String tile() {
        return bottomTile;
    }
}
