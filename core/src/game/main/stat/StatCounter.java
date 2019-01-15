package game.main.stat;

public interface StatCounter {
    float count(Stat.Type type);
    StatManager getStats();
}
