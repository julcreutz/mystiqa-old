package mystiqa.entity;

public enum Alignment {
    EVIL,
    NEUTRAL,
    GOOD;

    public boolean isHostile(Alignment other) {
        switch (this) {
            case EVIL:
                return other != EVIL;
            case NEUTRAL:
                return false;
            case GOOD:
                return other == EVIL;
        }

        return false;
    }
}
