package mystiqa.world.biome;

import com.badlogic.gdx.utils.JsonValue;

public class Vegetation {
    public float chance;
    public float minDist;

    public String structure;

    public void deserialize(JsonValue json) {
        if (json.has("chance")) {
            chance = json.getFloat("chance");
        }

        if (json.has("minDist")) {
            minDist = json.getFloat("minDist");
        }

        if (json.has("structure")) {
            structure = json.getString("structure");
        }
    }
}
