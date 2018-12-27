package game.main.play.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.SheetLoader;

public class TileType {
    public String name;

    public TextureRegion[][] sheet;
    public String[] colors;

    public boolean autoTile;

    public String[] connect;

    public boolean solid;

    public boolean connect(TileType type) {
        if (connect != null) {
            for (String _connect : connect) {
                if (_connect.equals(type.name)) {
                    return true;
                }
            }
        }

        return name.equals(type.name);
    }

    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("sheet")) {
            sheet = SheetLoader.load(json.getString("sheet"));
        }

        if (json.has("colors")) {
            colors = json.get("colors").asStringArray();
        }

        if (json.has("autoTile")) {
            autoTile = json.getBoolean("autoTile");
        }

        if (json.has("connect")) {
            connect = json.get("connect").asStringArray();
        }

        if (json.has("solid")) {
            solid = json.getBoolean("solid");
        }
    }
}
