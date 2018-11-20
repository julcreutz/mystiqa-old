package mystiqa.world.structure;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class Structure {
    public StructureComponent[] components;

    public String getTile(int x, int y, int z) {
        String tile = null;

        for (StructureComponent component : components) {
            if (component.x == x && component.y == y && component.z == z) {
                tile = component.tile;
            }
        }

        return tile;
    }

    public void deserialize(JsonValue json) {
        if (json.has("components")) {
            Array<StructureComponent> components = new Array<StructureComponent>();

            for (JsonValue component : json.get("components")) {
                StructureComponent sc = new StructureComponent();
                sc.deserialize(component.child);
                components.add(sc);
            }

            this.components = new StructureComponent[components.size];
            for (int i = 0; i < components.size; i++) {
                this.components[i] = components.get(i);
            }
        }
    }
}
