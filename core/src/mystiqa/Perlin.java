package mystiqa;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

public class Perlin {
    public static float noise(float x, float y) {
        int x0 = (int) x;
        int y0 = (int) y;

        int x1 = x0 + 1;
        int y1 = y0 + 1;

        float wx = x - x0;
        float wy = y - y0;

        Random r = new Random();
        r.setSeed((long) (x * y));

        float v1 = new Vector2(x, y).sub(x0, y0).dot(-1 + r.nextFloat() * 2f, -1 + r.nextFloat() * 2f);
        float v2 = new Vector2(x, y).sub(x1, y0).dot(-1 + r.nextFloat() * 2f, -1 + r.nextFloat() * 2f);

        float i1 = MathUtils.lerp(v1, v2, wx);

        v1 = new Vector2(x, y).sub(x0, y1).dot(-1 + r.nextFloat() * 2f, -1 + r.nextFloat() * 2f);
        v2 = new Vector2(x, y).sub(x1, y1).dot(-1 + r.nextFloat() * 2f, -1 + r.nextFloat() * 2f);

        float i2 = MathUtils.lerp(v1, v2, wx);

        return MathUtils.lerp(i1, i2, wy);
    }

    public static float layeredNoise(float x, float y, int octaves, float freq, float freqScale, float amp, float ampScale) {
        float val = 0;

        for (int i = 0; i < octaves; i++) {
            val += noise(x * freq, y * freq) * amp;

            freq *= freqScale;
            amp *= ampScale;
        }

        return val;
    }
}
