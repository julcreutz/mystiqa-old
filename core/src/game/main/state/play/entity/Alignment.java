package game.main.state.play.entity;

public enum Alignment {
    GOOD, EVIL;

    public boolean isHostile(Alignment a) {
        return a != this;
    }
}
