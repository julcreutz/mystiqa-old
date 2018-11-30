package mystiqa.noise;

import com.badlogic.gdx.utils.JsonValue;

public class NoiseParameters {
    public int octaves;
    public float frequency;
    public float persistence;

    public NoiseParameters(int octaves, float frequency, float persistence) {
        this.octaves = octaves;
        this.frequency = frequency;
        this.persistence = persistence;
    }

    public NoiseParameters() {
        this(1, 1, 1);
    }

    public void deserialize(JsonValue json) {
        if (json.has("octaves")) {
            octaves = json.getInt("octaves");
        }

        if (json.has("frequency")) {
            frequency = json.getFloat("frequency");
        }

        if (json.has("persistence")) {
            persistence = json.getFloat("persistence");
        }
    }
}
