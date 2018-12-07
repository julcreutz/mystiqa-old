package engine.noise;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Two dimensional noise implementation based on Ken Perlin's original implementation
 * from 1997.
 */
public class Noise {
    /**
     * Holds the gradient vectors used for noise calculation.
     *
     * The gradient vectors are pre-computed in the constructor.
     */
    private Vector2[][] gradients;

    /**
     * Constructor initializing gradient vectors based on the given seed.
     *
     * @param seed seed used for gradient vector calculation
     */
    public Noise(long seed) {
        Random random = new Random(seed);

        gradients = new Vector2[256][256];

        Vector2[] possible = new Vector2[] {
                new Vector2(1, 0),
                new Vector2(0, 1),
                new Vector2(-1, 0),
                new Vector2(0, -1)
        };

        for (int xx = 0; xx < gradients.length; xx++) {
            for (int yy = 0; yy < gradients[0].length; yy++) {
                gradients[xx][yy] = possible[random.nextInt(possible.length - 1)];
            }
        }
    }

    /**
     * Returns combined noise value at given coordinates.
     *
     * The noise value is calculated by overlapping multiple
     * noise values given by params. This is used to create
     * various types of noise maps, e.g. flat or rough noise.
     *
     * @param x x coordinate of noise
     * @param y y coordinate of noise
     * @param params noise paramaters determining octaves, frequency and persistence
     * @return combined noise value at given coordinates
     */
    public float get(float x, float y, NoiseParameters params) {
        float val = 0;
        float total = 0;

        float freq = params.getFrequency();
        float amp = 1;

        for (int i = 0; i < params.getOctaves(); i++) {
            val += get(x * freq, y * freq) * amp;
            total += amp;

            float scale = 2;

            freq *= scale;

            amp *= 1f / scale;
            amp *= params.getPersistence();
        }

        return MathUtils.clamp(val / total, 0, 1);
    }

    /**
     * Returns noise value at given coordinates.
     *
     * The algorithm is based on Ken Perlin's implementation of Perlin Noise.
     * Pre-computed gradient vectors of the corners of the cell the given coordinates
     * are calculated. The final noise value is computed by bilinearly interpolating
     * the dot products of each corner gradient vector and the given coordinates.
     *
     * This implementation is adapted to return values from 0 to 1.
     *
     * @param x x coordinate of noise
     * @param y y coordinate of noise
     * @return noise value at given coordinates
     */
    private float get(float x, float y) {
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

    /**
     * Smoothly interpolates between two values.
     *
     * This method interpolates between two values. The interpolation
     * formula was originally proposed by Ken Perlin to improve
     * continuity of noise.
     *
     * @param from value to interpolate from
     * @param to value to interpolate to
     * @param t interpolation position
     * @return interpolated value
     */
    private float interpolate(float from, float to, float t) {
        return from + (to - from) * (6 * t * t * t * t * t - 15 * t * t * t * t + 10 * t * t * t);
    }
}
