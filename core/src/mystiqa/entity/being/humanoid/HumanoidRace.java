package mystiqa.entity.being.humanoid;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;
import mystiqa.stat.StatManager;

public class HumanoidRace {
    public TextureRegion[][] feet;
    public TextureRegion[][] body;
    public TextureRegion[][] head;

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

        if (json.has("stats")) {
            stats.deserialize(json.get("stats"));
        }
    }
}
