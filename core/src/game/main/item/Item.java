package game.main.item;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

/**
 * Superclass every type of item, e.g. weapons or consumables, must extend. Only consists of name and palette.
 */
public abstract class Item implements Serializable {
    /** Name of item. */
    public String name;

    @Override
    public void deserialize(JsonValue json) {
        JsonValue name = json.get("name");
        if (name != null) {
            this.name = name.asString();
        }
    }
}
