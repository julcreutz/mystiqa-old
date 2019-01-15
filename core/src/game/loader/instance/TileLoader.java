package game.loader.instance;

import com.badlogic.gdx.files.FileHandle;
import game.main.state.play.map.tile.ConnectedTile;
import game.main.state.play.map.tile.Tile;
import game.main.state.play.map.tile.UnconnectedTile;

public class TileLoader extends InstanceLoader<Tile> {
    public TileLoader(String root) {
        super(root);
    }

    @Override
    public Tile newInstance(String name) {
        if (name.equals("ConnectedTile")) {
            return new ConnectedTile();
        } else if (name.equals("UnconnectedTile")) {
            return new UnconnectedTile();
        }

        return null;
    }
}
