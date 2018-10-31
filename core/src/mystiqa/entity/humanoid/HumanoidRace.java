package mystiqa.entity.humanoid;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;

public class HumanoidRace {
    public TextureRegion[][] feet;
    public TextureRegion[][] body;
    public TextureRegion[][] head;

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
    }
}
