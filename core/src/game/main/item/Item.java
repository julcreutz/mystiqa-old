package game.main.item;

import game.SpriteSheet;

import java.io.Serializable;

/** Superclass every type of item, e.g. weapons or consumables, must extend. Only consists of name and icon. */
public abstract class Item implements Serializable {
    /** Name of item. */
    public String name;
    /** Item icon displayed in GUI elements. */
    public SpriteSheet icon;

    public String getName() {
        return name;
    }
}
