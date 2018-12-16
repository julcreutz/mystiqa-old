package game.main.site.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SiteTileType {
    public String name;

    public TextureRegion[][] sideSheet;
    public String[] sideColors;

    public TextureRegion[][] topSheet;
    public String[] topColors;

    public String[] connect;

    public boolean connect(SiteTileType type) {
        for (String _connect : connect) {
            if (_connect.equals(type.name)) {
                return true;
            }
        }

        return name.equals(type.name);
    }
}
