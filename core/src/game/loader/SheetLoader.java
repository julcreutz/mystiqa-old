package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

public class SheetLoader {
    private static HashMap<String, Texture> textures;
    private static HashMap<String, TextureRegion[][]> sheets;

    public static void load() {
        textures = new HashMap<String, Texture>();
        sheets = new HashMap<String, TextureRegion[][]>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/sheets.json"))) {
            Texture t = new Texture(Gdx.files.internal(json.getString("path")));
            textures.put(json.name, t);

            sheets.put(json.name, split(t, json.getInt("splitW"), json.getInt("splitH")));
        }
    }

    public static TextureRegion[][] load(String id) {
        return sheets.containsKey(id) ? sheets.get(id) : null;
    }

    private static TextureRegion[][] split(Texture image, int splitW, int splitH) {
        TextureRegion[][] sheet = new TextureRegion[image.getWidth() / splitW][image.getHeight() / splitH];

        for (int x = 0; x < sheet.length; x++) {
            for (int y = 0; y < sheet[0].length; y++) {
                sheet[x][y] = new TextureRegion(image, x * splitW, y * splitH, splitW, splitH);
            }
        }

        return sheet;
    }

    public static void dispose() {
        for (Texture t : textures.values()) {
            t.dispose();
        }
    }
}
