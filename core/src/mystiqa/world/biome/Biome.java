package mystiqa.world.biome;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.noise.Noise;
import mystiqa.noise.NoiseParameters;
import mystiqa.world.Tree;

public class Biome {
    public String name;

    public float targetElevation;
    public float targetTemperature;
    public float targetMoisture;

    public NoiseParameters elevation;
    public float noiseOffset;

    public int minHeight;
    public int maxHeight;
    public int heightOffset;

    public String waterTile;
    public String underWaterTile;
    public String aboveWaterTile;

    public NoiseParameters treeDensity;
    public NoiseParameters treeHeight;
    public Tree tree;

    public int getHeight(Noise noise, int x, int y) {
        return (int) MathUtils.lerp(minHeight, maxHeight, noise.get(x, y, elevation) + noiseOffset) + heightOffset;
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

        if (json.has("elevation")) {
            elevation = new NoiseParameters();
            elevation.deserialize(json.get("elevation"));
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

        if (json.has("treeDensity")) {
            treeDensity = new NoiseParameters();
            treeDensity.deserialize(json.get("treeDensity"));
        }

        if (json.has("treeHeight")) {
            treeHeight = new NoiseParameters();
            treeHeight.deserialize(json.get("treeHeight"));
        }

        if (json.has("tree")) {
            tree = new Tree();
            tree.deserialize(json.get("tree"));
        }
    }
}
