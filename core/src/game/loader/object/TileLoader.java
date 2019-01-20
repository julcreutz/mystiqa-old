package game.loader.object;

import game.main.object.tile.ConnectedTile;
import game.main.object.tile.Tile;
import game.main.object.tile.UnconnectedTile;

public class TileLoader extends ObjectLoader<Tile> {
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
