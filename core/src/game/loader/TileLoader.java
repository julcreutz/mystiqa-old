package game.loader;

import game.main.play.tile.TileType;

public class TileLoader extends ResourceLoader<TileType> {
    @Override
    public TileType newInstance() {
        return new TileType();
    }
}
