package game.main.state.play.map.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.state.play.Play;
import game.main.state.play.map.Map;
import game.main.state.play.map.tile.TileType;

import java.util.Random;

public abstract class Structure implements Serializable {
    public void generate(Random rand, Map map, int x, int y, int z) {

    }

    @Override
    public void deserialize(JsonValue json) {

    }

    public abstract TileType getTile();
}