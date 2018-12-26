package game.main.play.entity.humanoid;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.SheetLoader;
import game.loader.palette.PaletteShaderLoader;

public class HumanoidType {
    public TextureRegion[][] feet;
    public TextureRegion[][] body;
    public TextureRegion[][] head;

    public float animSpeed;

    public String[] colors;

    public void deserialize(JsonValue json) {
        if (json.has("feet")) {
            feet = SheetLoader.load(json.getString("feet"));
        }

        if (json.has("body")) {
            body = SheetLoader.load(json.getString("body"));
        }

        if (json.has("head")) {
            head = SheetLoader.load(json.getString("head"));
        }

        if (json.has("animSpeed")) {
            animSpeed = json.getFloat("animSpeed");
        }

        if (json.has("colors")) {
            colors = json.get("colors").asStringArray();
        }
    }
}
