package mystiqa.entity.being.humanoid.race;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;
import mystiqa.stat.StatManager;

public class HumanoidRace {
    public TextureRegion[][] feet;
    public TextureRegion[][] body;
    public TextureRegion[][] head;

    public Color color;

    public StatManager stats;

    public HumanoidRace() {
        stats = new StatManager();
    }

    public void deserialize(JsonValue json) {
        if (json.has("feet")) {
            feet = Resources.getSpriteSheet(json.getString("feet"));
        }

        if (json.has("body")) {
            body = Resources.getSpriteSheet(json.getString("body"));
        }

        if (json.has("head")) {
            head = Resources.getSpriteSheet(json.getString("head"));
        }

        if (json.has("color")) {
            color = Resources.getColor(json.getString("color"));
        }

        if (json.has("stats")) {
            stats.deserialize(json.get("stats"));
        }
    }
}
