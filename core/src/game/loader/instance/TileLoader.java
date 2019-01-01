package game.loader.instance;

import game.main.state.play.map.tile.Tile;

public class TileLoader extends InstanceLoader<Tile> {
    @Override
    public Tile newInstance(String name) {
        if (name.equals("Tile")) {
            return new Tile();
        }

        return null;
    }
}
