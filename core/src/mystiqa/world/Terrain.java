package mystiqa.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Perlin;

public class Terrain {
    public String name;

    public float targetElevation;

    public float frequency;
    public int octaves;
    public float persistence;

    public int minHeight;
    public int maxHeight;

    public Perlin perlin;

    public Terrain() {
        frequency = 1;
        octaves = 1;
        persistence = 1;

        perlin = new Perlin();
    }

    public int getHeight(int x, int y) {
        return (int) MathUtils.lerp(minHeight, maxHeight, perlin.layeredNoise(x, y, frequency, octaves, persistence));
    }

    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("targetElevation")) {
            targetElevation = json.getFloat("targetElevation");
        }

        if (json.has("frequency")) {
            frequency = json.getFloat("frequency");
        }

        if (json.has("octaves")) {
            octaves = json.getInt("octaves");
        }

        if (json.has("persistence")) {
            persistence = json.getFloat("persistence");
        }

        if (json.has("minHeight")) {
            minHeight = json.getInt("minHeight");
        }

        if (json.has("maxHeight")) {
            maxHeight = json.getInt("maxHeight");
        }
    }
}
