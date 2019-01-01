package game;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

import java.util.Random;

/** Class enclosing an interval with two inclusive boundaries. */
public class Range implements Serializable {
    /** Lower bound. Inclusive */
    public int min;

    /** Upper bound. Inclusive */
    public int max;

    /**
     * Constructs new instance and directly deserializes it from given json value.
     *
     * @param json json value
     */
    public Range(JsonValue json) {
        deserialize(json);
    }

    /**
     * Constructs new instance with given boundaries.
     *
     * @param min lower inclusive bound
     * @param max upper inclusive bound
     */
    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /** Constructs new instance with boundaries of approximately -infinite to infinite. */
    public Range() {
        this(-Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Checks whether given value is in range of bounds. The boundaries are inclusive, meaning the method will return
     * true if given value equals {@link #min} or {@link #max}.
     *
     * @param val value to be checked
     * @return whether value is within bounds
     */
    public boolean inRange(int val) {
        return val >= min && val <= max;
    }

    /**
     * Picks a random value within bounds. Uses given {@link Random} object for random number generation.
     *
     * @param rand random number generator
     * @return random value within bounds
     */
    public int pickRandom(Random rand) {
        return min + rand.nextInt(max - min + 1);
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue min = json.get("min");
        if (min != null) {
            this.min = min.asInt();
        }

        JsonValue max = json.get("max");
        if (max != null) {
            this.max = max.asInt();
        }
    }
}
