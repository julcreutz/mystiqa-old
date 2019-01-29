package game.main.item;

import game.SpriteSheet;

/** Superclass every type of item, e.g. weapons or consumables, must extend. Only consists of name and icon. */
public abstract class Item {
    /** Name of item. */
    public String name;
    /** Item icon displayed in GUI elements. */
    public SpriteSheet icon;
}
