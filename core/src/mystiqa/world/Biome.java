package mystiqa.world;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Assets;

public class Biome {
    public String name;

    public float targetTemperature;
    public float targetMoisture;

    public int heightOffset;

    public Array<Terrain> terrain;

    public String underWaterTile;
    public String aboveWaterTile;
    public String waterTile;

    public String[] structures;

    public Biome() {
        terrain = new Array<Terrain>();
    }

    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("targetTemperature")) {
            targetTemperature = json.getFloat("targetTemperature");
        }

        if (json.has("targetMoisture")) {
            targetMoisture = json.getFloat("targetMoisture");
        }

        if (json.has("heightOffset")) {
            heightOffset = json.getInt("heightOffset");
        }

        if (json.has("terrain")) {
            for (JsonValue terrain : json.get("terrain")) {
                this.terrain.add(Assets.getInstance().getTerrain(terrain.asString()));
            }
        }

        if (json.has("underWaterTile")) {
            underWaterTile = json.getString("underWaterTile");
        }

        if (json.has("aboveWaterTile")) {
            aboveWaterTile = json.getString("aboveWaterTile");
        }

        if (json.has("waterTile")) {
            waterTile = json.getString("waterTile");
        }
    }
}
