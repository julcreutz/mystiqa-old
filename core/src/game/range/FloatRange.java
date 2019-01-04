package game.range;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

import java.util.Random;

/** Class enclosing an interval with two inclusive boundaries. */
public class FloatRange implements Serializable {
    /** Lower bound. Inclusive */
    public float min;

    /** Upper bound. Inclusive */
    public float max;

    /**
     * Constructs new instance and directly deserializes it from given json value.
     *
     * @param json json value
     */
    public FloatRange(JsonValue json) {
        this();
        deserialize(json);
    }

    /**
     * Constructs new instance with given boundaries.
     *
     * @param min lower inclusive bound
     * @param max upper inclusive bound
     */
    public FloatRange(float min, float max) {
        this.min = min;
        this.max = max;
    }

    /** Constructs new instance with boundaries of approximately -infinite to infinite. */
    public FloatRange() {
        this(-Float.MAX_VALUE, Float.MAX_VALUE);
    }

    /**
     * Checks whether given value is in range of bounds. The boundaries are inclusive, meaning the method will return
     * true if given value equals {@link #min} or {@link #max}.
     *
     * @param val value to be checked
     * @return whether value is within bounds
     */
    public boolean inRange(float val) {
        return val >= min && val <= max;
    }

    /**
     * Picks a random value within bounds. Uses given {@link Random} object for random number generation.
     *
     * @param rand random number generator
     * @return random value within bounds
     */
    public float pickRandom(Random rand) {
        return min + rand.nextFloat() * (max - min);
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue min = json.get("min");
        if (min != null) {
            this.min = min.asFloat();
        }

        JsonValue max = json.get("max");
        if (max != null) {
            this.max = max.asFloat();
        }
    }
}
