package game.main.item.equipment;

import game.main.item.Item;
import game.main.stat.Stats;

public class Equipment extends Item {
    public Stats stats;

    public Equipment() {
        stats = new Stats();
    }
}
