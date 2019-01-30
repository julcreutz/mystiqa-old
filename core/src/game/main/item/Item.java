package game.main.item;

import game.SpriteSheet;

/** Superclass every type of item, e.g. weapons or consumables, must extend. Only consists of name and icon. */
public abstract class Item {
    public String name;
    public SpriteSheet icon;

    public String getName() {
        return name;
    }
}
