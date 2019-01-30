package game.main.item.equipment;

import game.main.entity.event.EntityEvent;
import game.main.entity.event.EntityListener;
import game.main.item.Item;
import game.main.stat.Stat;
import game.main.stat.StatCounter;
import game.main.stat.StatManager;

public abstract class Equipment extends Item implements StatCounter, EntityListener {
    public StatManager stats;

    public Equipment() {
        stats = new StatManager();
    }

    @Override
    public float count(Stat.Type type) {
        return stats.count(type);
    }

    @Override
    public StatManager getStats() {
        return stats;
    }

    @Override
    public void eventReceived(EntityEvent e) {
    }
}
