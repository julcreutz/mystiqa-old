package game.main.state.play.map.world;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.map.structure.Structure;
import game.main.state.play.map.tile.TileType;

import java.util.Random;

public class Biome implements Serializable {
    public float minElevation;
    public float maxElevation;

    public boolean possibleRiverSource;
    public float riverHorizontalChance;

    public float horizontalChance;
    public Array<RoomSize> roomSizes;

    public float randomConnectChance;

    public TileType ground;
    public Structure wall;

    public Array<Connector> connectors;

    public int[] wayThickness;

    public TileType river;
    public TileType riverBridge;

    public Array<RoomTemplate> templates;

    public RoomTemplate pickTemplate(Room r, Random rand) {
        RoomTemplate t = null;

        if (templates != null) {
            for (RoomTemplate template : templates) {
                if (template.width == r.w && template.height == r.h && rand.nextFloat() < template.chance) {
                    t = template;
                }
            }
        }

        return t;
    }

    public Connector getConnector(Connection connection) {
        Connector c = null;

        for (Connector connector : connectors) {
            if (connector.fits(connection)) {
                c = connector;
            }
        }

        return c;
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("minElevation")) {
            minElevation = json.getFloat("minElevation");
        }

        if (json.has("maxElevation")) {
            maxElevation = json.getFloat("maxElevation");
        }

        if (json.has("possibleRiverSource")) {
            possibleRiverSource = json.getBoolean("possibleRiverSource");
        }

        if (json.has("riverHorizontalChance")) {
            riverHorizontalChance = json.getFloat("riverHorizontalChance");
        }

        if (json.has("horizontalChance")) {
            horizontalChance = json.getFloat("horizontalChance");
        }

        if (json.has("roomSizes")) {
            roomSizes = new Array<RoomSize>();

            for (JsonValue roomSize : json.get("roomSizes")) {
                RoomSize _roomSize = new RoomSize();
                _roomSize.deserialize(roomSize);
                roomSizes.add(_roomSize);
            }
        }

        if (json.has("randomConnectChance")) {
            randomConnectChance = json.getFloat("randomConnectChance");
        }

        if (json.has("ground")) {
            ground = Game.TILES.load(json.getString("ground"));
        }

        if (json.has("wall")) {
            wall = Game.STRUCTURES.load(json.getString("wall"));
        }

        if (json.has("connectors")) {
            connectors = new Array<Connector>();

            for (JsonValue connector : json.get("connectors")) {
                Connector c = new Connector();
                c.deserialize(connector);

                connectors.add(c);
            }
        }

        if (json.has("wayThickness")) {
            wayThickness = json.get("wayThickness").asIntArray();
        }

        if (json.has("river")) {
            river = Game.TILES.load(json.getString("river"));
        }

        if (json.has("riverBridge")) {
            riverBridge = Game.TILES.load(json.getString("riverBridge"));
        }

        if (json.has("templates")) {
            templates = new Array<RoomTemplate>();

            for (JsonValue template : json.get("templates")) {
                RoomTemplate t = new RoomTemplate();
                t.deserialize(template);
                templates.add(t);
            }
        }
    }
}
