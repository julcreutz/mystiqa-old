package game.main.item;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public abstract class Item implements Serializable {
    public String name;
    public ShaderProgram palette;

    @Override
    public void deserialize(JsonValue json) {

    }
}
