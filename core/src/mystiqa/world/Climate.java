package mystiqa.world;

import com.badlogic.gdx.utils.JsonValue;

public class Climate {
    public String name;

    public float targetTemperature;

    public String waterTile;
    public String underWaterTile;
    public String aboveWaterTile;

    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("targetTemperature")) {
            targetTemperature = json.getFloat("targetTemperature");
        }

        if (json.has("waterTile")) {
            waterTile = json.getString("waterTile");
        }

        if (json.has("underWaterTile")) {
            underWaterTile = json.getString("underWaterTile");
        }

        if (json.has("aboveWaterTile")) {
            aboveWaterTile = json.getString("aboveWaterTile");
        }
    }
}
