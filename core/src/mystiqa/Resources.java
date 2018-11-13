package mystiqa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class Resources {
    private static HashMap<String, Texture> textures;
    private static HashMap<Texture, Array<TextureRegion[][]>> spriteSheets;

    public static TextureRegion[][] getSpriteSheet(String path, int splitX, int splitY) {
        if (textures == null) {
            textures = new HashMap<String, Texture>();
        }

        if (!textures.containsKey(path)) {
            textures.put(path, new Texture(Gdx.files.internal(path)));
        }

        Texture t = textures.get(path);

        int w = t.getWidth() / splitX;
        int h = t.getHeight() / splitY;

        if (spriteSheets == null) {
            spriteSheets = new HashMap<Texture, Array<TextureRegion[][]>>();
        }

        if (!spriteSheets.containsKey(t)) {
            spriteSheets.put(t, new Array<TextureRegion[][]>());
        }

        for (TextureRegion[][] spriteSheet : spriteSheets.get(t)) {
            if (spriteSheet.length == w && spriteSheet[0].length == h) {
                return spriteSheet;
            }
        }

        TextureRegion[][] spriteSheet = new TextureRegion[w][h];
        for (int x = 0; x < spriteSheet.length; x++) {
            for (int y = 0; y < spriteSheet[0].length; y++) {
                spriteSheet[x][y] = new TextureRegion(t, x * splitX, y * splitY, splitX, splitY);
            }
        }

        spriteSheets.get(t).add(spriteSheet);

        return spriteSheet;
    }
}
