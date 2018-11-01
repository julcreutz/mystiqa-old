package mystiqa.item.equipable.material;

import com.badlogic.gdx.utils.JsonValue;
import mystiqa.stat.StatManager;

public class Material {
    public String name;

    public StatManager stats;

    public Material() {
        stats = new StatManager();
    }

    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("stats")) {
            stats.deserialize(json.get("stats"));
        }
    }
}
