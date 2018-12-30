package game;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

import java.util.Random;

public class Range implements Serializable {
    public int min;
    public int max;

    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public Range() {
        this(-Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public boolean inRange(int val) {
        return val >= min && val <= max;
    }

    public int pickRandom(Random rand) {
        return min + rand.nextInt(max - min + 1);
    }

    @Override
    public void deserialize(JsonValue json) {
        min = json.getInt("min", min);
        max = json.getInt("max", max);
    }
}
