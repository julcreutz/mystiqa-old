package game.main.item;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;

public abstract class Item implements Serializable {
    public String name;
    public ShaderProgram palette;

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("palette")) {
            palette = Game.PALETTES.load(json.get("palette").asStringArray());
        }
    }
}
