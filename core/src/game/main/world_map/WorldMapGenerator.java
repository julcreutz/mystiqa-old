package game.main.world_map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.loader.WorldMapPlayerTypeLoader;
import game.loader.WorldMapTileTypeLoader;
import game.main.world_map.entity.WorldMapEntity;
import game.main.world_map.entity.WorldMapPlayer;
import game.main.world_map.entity.WorldMapPlayerType;
import game.main.world_map.tile.WorldMapTile;
import game.main.world_map.tile.WorldMapTileType;
import game.noise.Noise;
import game.noise.NoiseParameters;

public class WorldMapGenerator {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    public static final NoiseParameters ELEVATION = new NoiseParameters(6, .05f, 1);

    public static void generate(WorldMap map) {
        Noise noise = new Noise(MathUtils.random(0));

        map.tiles = new WorldMapTile[WIDTH][HEIGHT];

        for (int x = 0; x < map.tiles.length; x++) {
            for (int y = 0; y < map.tiles[0].length; y++) {
                float f = getElevation(noise, ELEVATION, map, x, y);

                WorldMapTileType type;

                if (f > .5f) {
                    type = WorldMapTileTypeLoader.load("Mountains");
                } else if (f > .475f) {
                    type = WorldMapTileTypeLoader.load("Hills");
                } else if (f > .4f) {
                    type = WorldMapTileTypeLoader.load("Tree");
                } else if (f > .35f) {
                    type = WorldMapTileTypeLoader.load("Grass");
                } else if (f > .325f) {
                    type = WorldMapTileTypeLoader.load("Sand");
                } else {
                    type = WorldMapTileTypeLoader.load("Water");
                }

                map.tiles[x][y] = new WorldMapTile(type, x, y);
            }
        }

        map.entities = new Array<WorldMapEntity>();

        int x;
        int y;

        do {
            x = MathUtils.random(map.tiles.length - 1);
            y = MathUtils.random(map.tiles[0].length - 1);
        } while (map.tiles[x][y] != null && !map.tiles[x][y].type.name.equals("Grass"));

        WorldMapPlayer player = new WorldMapPlayer(WorldMapPlayerTypeLoader.load("Human"), x * 8, y * 8);
        map.player = player;
        map.entities.add(player);

        map.cursorX = x;
        map.cursorY = y;

        map.cam.position.x = x * 8 + 4;
        map.cam.position.y = y * 8 + 4;
    }

    public static float getElevation(Noise noise, NoiseParameters elevation, WorldMap map, int x, int y) {
        return noise.get(x, y, elevation) * (1 - new Vector2(x, y).sub(map.tiles.length * .5f, map.tiles[0].length * .5f).len() / (float) (Math.sqrt(map.tiles.length * map.tiles.length + map.tiles[0].length * map.tiles[0].length) * .5f));
    }
}
