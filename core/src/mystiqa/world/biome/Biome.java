package mystiqa.world.biome;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Perlin;
import mystiqa.main.screen.Play;

public class Biome {
    public String name;

    public float targetElevation;
    public float targetTemperature;
    public float targetMoisture;

    public Perlin noise;
    public float noiseOffset;

    public int minHeight;
    public int maxHeight;
    public int heightOffset;

    public String waterTile;
    public String underWaterTile;
    public String aboveWaterTile;

    public Array<Vegetation> vegetations;

    public Biome() {
        vegetations = new Array<Vegetation>();
    }

    public int getHeight(int x, int y) {
        return (int) MathUtils.lerp(minHeight, maxHeight, MathUtils.clamp(noise.get(x, y) + noiseOffset, 0, 1)) + heightOffset;
    }

    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("targetElevation")) {
            targetElevation = json.getFloat("targetElevation");
        }

        if (json.has("targetTemperature")) {
            targetTemperature = json.getFloat("targetTemperature");
        }

        if (json.has("targetMoisture")) {
            targetMoisture = json.getFloat("targetMoisture");
        }

        if (json.has("noise")) {
            noise = new Perlin(1, 1, 1, Play.getInstance().worldGenerator.seed());
            noise.deserialize(json.get("noise"));
        }

        if (json.has("minHeight")) {
            minHeight = json.getInt("minHeight");
        }

        if (json.has("maxHeight")) {
            maxHeight = json.getInt("maxHeight");
        }

        if (json.has("waterTile")) {
            waterTile = json.getString("waterTile");
        }

        if (json.has("underWaterTile")) {
            underWaterTile = json.getString("underWaterTile");
        }

        if (json.has("aboveWaterTile")) {
            aboveWaterTile = json.getString("aboveWaterTile");
        }

        if (json.has("vegetations")) {
            for (JsonValue vegetation : json.get("vegetations")) {
                Vegetation _vegetation = new Vegetation();
                _vegetation.deserialize(vegetation);

                vegetations.add(_vegetation);
            }
        }
    }
}
