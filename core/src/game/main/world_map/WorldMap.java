package game.main.world_map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import game.main.Game;
import game.main.GameState;

public class WorldMap extends GameState {
    public WorldMapTile[][] tiles;
    public Array<WorldMapEntity> entities;

    @Override
    public void create() {
        super.create();

        WorldMapGenerator.generate(this);
    }

    @Override
    public void update(Game g) {
        super.update(g);

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            cam.translate(-32 * Game.getDelta(), 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            cam.translate(32 * Game.getDelta(), 0);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            cam.translate(0, -32 * Game.getDelta());
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            cam.translate(0, 32 * Game.getDelta());
        }

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                WorldMapTile tile = tiles[x][y];

                if (tile != null) {
                    tile.update(this);
                }
            }
        }

        for (WorldMapEntity e : entities) {
            e.update(this);
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

        for (WorldMapEntity e : entities) {
            e.render(batch);
        }
    }
}
