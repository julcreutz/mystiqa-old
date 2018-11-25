package mystiqa.world.structure;

import com.badlogic.gdx.utils.JsonValue;

public class StructureComponent {
    public int x;
    public int y;
    public int z;

    public String tile;

    public void deserialize(JsonValue json) {
        if (json.has("x")) {
            x = json.getInt("x");
        }

        if (json.has("y")) {
            y = json.getInt("y");
        }

        if (json.has("z")) {
            z = json.getInt("z");
        }

        if (json.has("tile")) {
            tile = json.getString("tile");
        }
    }
}
