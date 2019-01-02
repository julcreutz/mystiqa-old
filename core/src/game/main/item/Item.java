package game.main.item;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;

/**
 * Superclass every type of item, e.g. weapons or consumables, must extend. Only consists of name and palette.
 */
public abstract class Item implements Serializable {
    /** Name of item. */
    public String name;
    /** Palette of item. */
    public ShaderProgram palette;

    @Override
    public void deserialize(JsonValue json) {
        JsonValue name = json.get("name");
        if (name != null) {
            this.name = name.asString();
        }

        JsonValue palette = json.get("palette");
        if (palette != null) {
            this.palette = Game.PALETTES.load(palette.asStringArray());
        }
    }
}
