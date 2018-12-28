package game.main.gen;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.structure.Structure;
import game.main.state.play.tile.TileType;

public class Biome implements Serializable {
    public float minElevation;
    public float maxElevation;

    public float horizontalChance;
    public Array<RoomSize> roomSizes;

    public TileType ground;
    public Structure wall;

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("minElevation")) {
            minElevation = json.getFloat("minElevation");
        }

        if (json.has("maxElevation")) {
            maxElevation = json.getFloat("maxElevation");
        }

        if (json.has("horizontalChance")) {
            horizontalChance = json.getFloat("horizontalChance");
        }

        if (json.has("roomSizes")) {
            roomSizes = new Array<>();

            for (JsonValue roomSize : json.get("roomSizes")) {
                RoomSize _roomSize = new RoomSize();
                _roomSize.deserialize(roomSize);
                roomSizes.add(_roomSize);
            }
        }

        if (json.has("ground")) {
            ground = Game.TILES.load(json.getString("ground"));
        }

        if (json.has("wall")) {
            wall = Game.STRUCTURES.load(json.getString("wall"));
        }
    }
}
