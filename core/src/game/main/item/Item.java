package game.main.item;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;

/** Superclass every type of item, e.g. weapons or consumables, must extend. Only consists of name and icon. */
public abstract class Item implements Serializable {
    /** Name of item. */
    public String name;
    /** Item icon. */
    public SpriteSheet icon;

    @Override
    public void deserialize(JsonValue json) {
        JsonValue name = json.get("name");
        if (name != null) {
            this.name = name.asString();
        }

        JsonValue icon = json.get("icon");
        if (icon != null) {
            this.icon = Game.SPRITE_SHEETS.load(icon.asString());
        }
    }
}
