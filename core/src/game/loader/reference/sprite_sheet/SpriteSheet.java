package game.loader.reference.sprite_sheet;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class SpriteSheet implements Serializable {
    private String path;
    private int columns;
    private int rows;

    private Texture source;
    private TextureRegion[][] split;

    public SpriteSheet() {
        columns = 1;
        rows = 1;
    }

    public int getColumns() {
        return columns;
    }

    public int getRows() {
        return rows;
    }

    public Texture getSource() {
        if (source == null) {
            source = new Texture(this.path);
        }

        return source;
    }

    public TextureRegion[][] getSplit() {
        if (split == null) {
            Texture source = getSource();

            int splitW = source.getWidth() / columns;
            int splitH = source.getHeight() / rows;

            split = new TextureRegion[source.getWidth() / splitW][source.getHeight() / splitH];

            for (int x = 0; x < split.length; x++) {
                for (int y = 0; y < split[0].length; y++) {
                    split[x][y] = new TextureRegion(source, x * splitW, y * splitH, splitW, splitH);
                }
            }
        }

        return split;
    }

    public TextureRegion grab(int column, int row) {
        return getSplit()[column][row];
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue path = json.get("path");
        if (path != null) {
            this.path = path.asString();
        }

        JsonValue columns = json.get("columns");
        if (columns != null) {
            this.columns = columns.asInt();
        }

        JsonValue rows = json.get("rows");
        if (rows != null) {
            this.rows = rows.asInt();
        }
    }
}
