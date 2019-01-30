package game.main.item.equipment.attribute;

import game.main.stat.HasStats;
import game.main.stat.Stats;

public class Attribute implements HasStats {
    public String name;
    public Stats stats;

    public Attribute() {
        stats = new Stats();
    }

    @Override
    public Stats getStats() {
        return stats;
    }
}
