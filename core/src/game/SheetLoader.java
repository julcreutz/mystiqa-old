package game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SheetLoader {
    public static TextureRegion[][] split(Texture image, int splitW, int splitH) {
        TextureRegion[][] sheet = new TextureRegion[image.getWidth() / splitW][image.getHeight() / splitH];

        for (int x = 0; x < sheet.length; x++) {
            for (int y = 0; y < sheet[0].length; y++) {
                sheet[x][y] = new TextureRegion(image, x * splitW, y * splitH, splitW, splitH);
            }
        }

        return sheet;
    }
}
