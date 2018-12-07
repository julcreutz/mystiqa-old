package game.main.world_map;

import game.main.Game;
import game.main.GameState;

public class WorldMap extends GameState {
    public WorldMapTile[][] tiles;

    @Override
    public void create() {
        super.create();

        tiles = new WorldMapTile[64][64];
    }

    @Override
    public void update(Game g) {
        super.update(g);

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                WorldMapTile tile = tiles[x][y];

                if (tile != null) {
                    tile.update(this);
                }
            }
        }
    }

    @Override
    public void renderToBuffer() {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                WorldMapTile tile = tiles[x][y];

                if (tile != null) {
                    tile.render(batch);
                }
            }
        }
    }
}
