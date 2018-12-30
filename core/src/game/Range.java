package game;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

import java.util.Random;

/** Class enclosing interval with two inclusive boundaries. */
public class Range implements Serializable {
    /** Lower bound. */
    public int min;

    /** Higher bound. */
    public int max;

    /** Constructs range with given boundaries. */
    public Range(int min, int max) {
        this.min = min;
        this.max = max;
    }

    /** Constructs range with boundaries of approximately -infinite to infinite. */
    public Range() {
        this(-Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Checks whether given value is in range of bounds.
     *
     * @param val value to be checked
     * @return whether value is within bounds
     */
    public boolean inRange(int val) {
        return val >= min && val <= max;
    }

    /**
     * Picks a random value within bounds of range. Uses given
     * {@link Random} object for random number generation.
     *
     * @param rand random number generator
     * @return random value within bounds
     */
    public int pickRandom(Random rand) {
        return min + rand.nextInt(max - min + 1);
    }

    @Override
    public void deserialize(JsonValue json) {
        min = json.getInt("min", min);
        max = json.getInt("max", max);
    }
}
