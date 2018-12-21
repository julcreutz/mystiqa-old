package game.main.gen;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.StructureLoader;
import game.loader.TileLoader;
import game.main.play.structure.Structure;
import game.main.play.tile.TileType;

public class Biome {
    public float minElevation;
    public float maxElevation;

    public TileType ground;
    public Structure wall;

    public void deserialize(JsonValue json) {
        if (json.has("minElevation")) {
            minElevation = json.getFloat("minElevation");
        }

        if (json.has("maxElevation")) {
            maxElevation = json.getFloat("maxElevation");
        }

        if (json.has("ground")) {
            ground = TileLoader.load(json.getString("ground"));
        }

        if (json.has("wall")) {
            wall = StructureLoader.load(json.getString("wall"));
        }
    }
}
