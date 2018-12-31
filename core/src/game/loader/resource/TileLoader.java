package game.loader.resource;

import game.main.state.play.map.tile.Tile;

public class TileLoader extends ResourceLoader<Tile.Type> {
    @Override
    public Tile.Type newInstance() {
        return new Tile.Type();
    }
}
