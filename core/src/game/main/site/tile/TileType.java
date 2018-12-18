package game.main.site.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

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
}
