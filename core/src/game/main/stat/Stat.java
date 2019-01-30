package game.main.stat;

public class Stat {
    public enum Type {
        HEALTH,
        SPEED,
        PHYSICAL_DAMAGE, PHYSICAL_DEFENSE,
        FIRE_DAMAGE, FIRE_RESISTANCE
    }

    public Type type;

    public float value;
    public float multiplier;

    public Stat(Type type, float value, float multiplier) {
        this.type = type;
        this.value = value;
        this.multiplier = multiplier;
    }

    public Stat(Type type, float value) {
        this(type, value, 0);
    }

    public Stat(Type type) {
        this(type, 0, 0);
    }
}
