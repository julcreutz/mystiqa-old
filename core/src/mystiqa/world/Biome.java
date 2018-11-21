package mystiqa.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Assets;
import mystiqa.Perlin;

public class Biome {
    public String name;

    public float targetTemperature;
    public float targetMoisture;

    public float targetElevation;

    public int minHeight;
    public int maxHeight;

    public int heightOffset;

    public String underWaterTile;
    public String aboveWaterTile;
    public String waterTile;

    public Perlin noise;

    public int getHeight(float x, float y) {
        return (int) MathUtils.lerp(minHeight, maxHeight, noise.get(x, y));
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

        if (json.has("targetElevation")) {
            targetElevation = json.getFloat("targetElevation");
        }

        if (json.has("minHeight")) {
            minHeight = json.getInt("minHeight");
        }

        if (json.has("maxHeight")) {
            maxHeight = json.getInt("maxHeight");
        }

        if (json.has("heightOffset")) {
            heightOffset = json.getInt("heightOffset");
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

        if (json.has("noise")) {
            noise = new Perlin();
            noise.deserialize(json.get("noise"));
        }
    }
}
