package game.main.state.play.map.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.state.play.map.Map;
import game.main.state.play.map.tile.Tile;

import java.util.Random;

public abstract class Structure<T extends Map> implements Serializable {
    public String tile() {
        return null;
    }

    public abstract void generate(Random rand, T map, int x, int y, int z);

    @Override
    public abstract void deserialize(JsonValue json);
}
