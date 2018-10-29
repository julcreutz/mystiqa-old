package mystiqa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.main.Game;

public class Resources {
    public static TextureRegion[][] getSpriteSheet(String name) {
        Array<FileHandle> files = new Array<FileHandle>();
        Game.getFiles(Gdx.files.internal("data/sprite_sheets/"), files);

        for (FileHandle file : files) {
            if (file.nameWithoutExtension().equals(name)) {
                JsonValue root = new JsonReader().parse(file);

                Texture t = new Texture(Gdx.files.internal(root.getString("path")));

                int splitX = root.getInt("splitX");
                int splitY = root.getInt("splitX");

                TextureRegion[][] spriteSheet = new TextureRegion[t.getWidth() / splitX][t.getHeight() / splitY];

                for (int x = 0; x < spriteSheet.length; x++) {
                    for (int y = 0; y < spriteSheet[0].length; y++) {
                        spriteSheet[x][y] = new TextureRegion(t, x * splitX, y * splitY, splitX, splitY);
                    }
                }

                return spriteSheet;
            }
        }

        return null;
    }
}
