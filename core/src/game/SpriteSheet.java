package game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.main.Game;

public class SpriteSheet {
    private TextureRegion[][] split;

    public SpriteSheet(String path, int columns, int rows) {
        Texture t = Game.TEXTURES.load(path);

        int splitW = t.getWidth() / columns;
        int splitH = t.getHeight() / rows;

        split = new TextureRegion[t.getWidth() / splitW][t.getHeight() / splitH];

        for (int x = 0; x < split.length; x++) {
            for (int y = 0; y < split[0].length; y++) {
                split[x][y] = new TextureRegion(t, x * splitW, y * splitH, splitW, splitH);
            }
        }
    }

    public SpriteSheet(String path) {
        this(path, 1, 1);
    }

    public int getColumns() {
        return split.length;
    }

    public int getRows() {
        return split[0].length;
    }

    public TextureRegion grab(int column, int row) {
        return split[MathUtils.clamp(column, 0, getColumns() - 1)][MathUtils.clamp(row, 0, getRows() - 1)];
    }
}
