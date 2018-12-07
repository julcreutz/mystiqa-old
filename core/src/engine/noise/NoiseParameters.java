package engine.noise;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Parameters used for noise calculation.
 * @see Noise
 */
public class NoiseParameters {
    /**
     * Numbers of overlapped octaves.
     * The higher the number of octaves, the rougher the noise.
     */
    private int octaves;

    /**
     * Controls frequency of noise.
     * The higher the frequency, the noisier the value.
     */
    private float frequency;

    /**
     * Determines how much each octave affects total noise value.
     * Experiment to see effects.
     */
    private float persistence;

    /**
     * Constructor initializing octaves, frequency and persistence.
     *
     * @param octaves number of octaves
     * @param frequency frequency
     * @param persistence persistence
     */
    public NoiseParameters(int octaves, float frequency, float persistence) {
        this.octaves = octaves;
        this.frequency = frequency;
        this.persistence = persistence;
    }

    /**
     * @return number of octaves.
     */
    public int getOctaves() {
        return octaves;
    }

    /**
     * @return frequency
     */
    public float getFrequency() {
        return frequency;
    }

    /**
     * @return persistence
     */
    public float getPersistence() {
        return persistence;
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
