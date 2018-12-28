package game.main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class SpriteSheet implements Serializable {
    public Texture texture;
    public TextureRegion[][] sheet;

    @Override
    public void deserialize(JsonValue json) {
        texture = new Texture(Gdx.files.internal(json.getString("path")));
        sheet = split(texture, json.getInt("splitW"), json.getInt("splitH"));
    }

    private TextureRegion[][] split(Texture image, int splitW, int splitH) {
        TextureRegion[][] sheet = new TextureRegion[image.getWidth() / splitW][image.getHeight() / splitH];

        for (int x = 0; x < sheet.length; x++) {
            for (int y = 0; y < sheet[0].length; y++) {
                sheet[x][y] = new TextureRegion(image, x * splitW, y * splitH, splitW, splitH);
            }
        }

        return sheet;
    }
}
