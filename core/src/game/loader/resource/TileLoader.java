package game.loader.resource;

import game.main.state.play.map.tile.TileType;

public class TileLoader extends ResourceLoader<TileType> {
    @Override
    public TileType newInstance() {
        return new TileType();
    }
}
