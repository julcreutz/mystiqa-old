package mystiqa.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Perlin {
    private Vector2[][] gradients;

    public float noise(float x, float y) {
        if (gradients == null) {
            gradients = new Vector2[16][16];

            for (int xx = 0; xx < gradients.length; xx++) {
                for (int yy = 0; yy < gradients[0].length; yy++) {
                    gradients[xx][yy] = getRandomGradient();
                }
            }
        }

        int x0 = (int) x;
        int y0 = (int) y;

        int x1 = x0 + 1;
        int y1 = y0 + 1;

        float wx = x - x0;
        float wy = y - y0;

        int gx0 = x0 % gradients.length;
        int gx1 = x1 % gradients.length;

        int gy0 = y0 % gradients[0].length;
        int gy1 = y1 % gradients[0].length;

        float v1 = new Vector2(x, y).sub(x0, y0).dot(gradients[gx0][gy0]);
        float v2 = new Vector2(x, y).sub(x1, y0).dot(gradients[gx1][gy0]);

        float i1 = MathUtils.lerp(v1, v2, wx);

        v1 = new Vector2(x, y).sub(x0, y1).dot(gradients[gx0][gy1]);
        v2 = new Vector2(x, y).sub(x1, y1).dot(gradients[gx1][gy1]);

        float i2 = MathUtils.lerp(v1, v2, wx);

        return MathUtils.lerp(i1, i2, wy) + 0.5f;
    }

    public float layeredNoise(float x, float y, float freq, int octaves, float persistence) {
        float val = 0;
        float total = 0;

        float amp = 1;

        for (int i = 0; i < octaves; i++) {
            val += noise(x * freq, y * freq) * amp;
            total += amp;

            float scale = 2;

            freq *= scale;

            amp *= 1f / scale;
            amp *= persistence;
        }

        return val / total;
    }

    private Vector2 getRandomGradient() {
        Vector2[] possible = new Vector2[] {
                new Vector2(1, 0),
                new Vector2(0, 1),
                new Vector2(-1, 0),
                new Vector2(0, -1)
        };

        return possible[MathUtils.random(possible.length - 1)];
    }
}
