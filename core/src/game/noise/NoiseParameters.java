package game.noise;

public class NoiseParameters {
    public int octaves;
    public float frequency;
    public float persistence;

    public NoiseParameters(int octaves, float frequency, float persistence) {
        this.octaves = octaves;
        this.frequency = frequency;
        this.persistence = persistence;
    }
}
