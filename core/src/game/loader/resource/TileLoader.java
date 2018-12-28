package game.loader.resource;

import game.main.play.tile.TileType;

public class TileLoader extends ResourceLoader<TileType> {
    @Override
    public TileType newInstance() {
        return new TileType();
    }
}
