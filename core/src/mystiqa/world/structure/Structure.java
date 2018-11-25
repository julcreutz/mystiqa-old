package mystiqa.world.structure;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;

public class Structure {
    public Array<StructureComponent> components;

    public void deserialize(JsonValue json) {
        if (json.has("components")) {
            components = new Array<StructureComponent>();

            for (JsonValue component : json.get("components")) {
                StructureComponent _component = new StructureComponent();
                _component.deserialize(component);

                components.add(_component);
            }
        }
    }
}
