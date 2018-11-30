package mystiqa;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;

import java.util.Random;

public class Perlin {
    private Vector2[][] gradients;

    private Random random;

    public float frequency;
    public int octaves;
    public float persistence;

    public Perlin(float frequency, int octaves, float persistence, long seed) {
        this.frequency = frequency;
        this.octaves = octaves;
        this.persistence = persistence;

        random = new Random(seed);
    }

    private float noise(float x, float y) {
        if (gradients == null) {
            gradients = new Vector2[256][256];

            for (int xx = 0; xx < gradients.length; xx++) {
                for (int yy = 0; yy < gradients[0].length; yy++) {
                    gradients[xx][yy] = randomGradient();
                }
            }
        }

        int x0 = MathUtils.floor(x);
        int y0 = MathUtils.floor(y);

        int x1 = x0 + 1;
        int y1 = y0 + 1;

        float wx = x - x0;
        float wy = y - y0;

        int gx0 = x0;
        while (gx0 < 0) {
            gx0 += gradients.length;
        }
        gx0 %= gradients.length;

        int gx1 = x1;
        while (gx1 < 0) {
            gx1 += gradients.length;
        }
        gx1 %= gradients.length;

        int gy0 = y0;
        while (gy0 < 0) {
            gy0 += gradients[0].length;
        }
        gy0 %= gradients[0].length;

        int gy1 = y1;
        while (gy1 < gradients[0].length) {
            gy1 += gradients[0].length;
        }
        gy1 %= gradients[0].length;

        float v1 = new Vector2(x, y).sub(x0, y0).dot(gradients[gx0][gy0]);
        float v2 = new Vector2(x, y).sub(x1, y0).dot(gradients[gx1][gy0]);

        float i1 = interpolate(v1, v2, wx);

        v1 = new Vector2(x, y).sub(x0, y1).dot(gradients[gx0][gy1]);
        v2 = new Vector2(x, y).sub(x1, y1).dot(gradients[gx1][gy1]);

        float i2 = interpolate(v1, v2, wx);

        return MathUtils.clamp(interpolate(i1, i2, wy) + 0.5f, 0, 1);
    }

    public float get(float x, float y) {
        float val = 0;
        float total = 0;

        float freq = frequency;
        float amp = 1;

        for (int i = 0; i < octaves; i++) {
            val += noise(x * freq, y * freq) * amp;
            total += amp;

            float scale = 2;

            freq *= scale;

            amp *= 1f / scale;
            amp *= persistence;
        }

        return MathUtils.clamp(val / total, 0, 1);
    }

    private Vector2 randomGradient() {
        Vector2[] possible = new Vector2[] {
                new Vector2(1, 0),
                new Vector2(0, 1),
                new Vector2(-1, 0),
                new Vector2(0, -1)
        };

        return possible[random.nextInt(possible.length - 1)];
    }

    private float interpolate(float from, float to, float t) {
        return from + (to - from) * (6 * t * t * t * t * t - 15 * t * t * t * t + 10 * t * t * t);
    }

    public void deserialize(JsonValue json) {
        if (json.has("frequency")) {
            frequency = json.getFloat("frequency");
        }

        if (json.has("octaves")) {
            octaves = json.getInt("octaves");
        }

        if (json.has("persistence")) {
            persistence = json.getFloat("persistence");
        }
    }
}
