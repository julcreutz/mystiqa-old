package mystiqa.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Perlin;

public class Terrain {
    public String name;

    public float targetElevation;

    public Perlin noise;
    public float noiseOffset;

    public int minHeight;
    public int maxHeight;
    public int heightOffset;

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

        if (json.has("noise")) {
            noise = new Perlin();
            noise.deserialize(json.get("noise"));
        }

        if (json.has("minHeight")) {
            minHeight = json.getInt("minHeight");
        }

        if (json.has("maxHeight")) {
            maxHeight = json.getInt("maxHeight");
        }
    }
}
