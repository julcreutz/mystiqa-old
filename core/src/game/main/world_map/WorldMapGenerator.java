package game.main.world_map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.loader.WorldMapEntityTypeLoader;
import game.loader.WorldMapTileTypeLoader;
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
        map.entities.add(new WorldMapEntity(WorldMapEntityTypeLoader.load("Player"), 0, 0));
    }

    public static float getElevation(Noise noise, NoiseParameters elevation, WorldMap map, int x, int y) {
        float dist = new Vector2(x, y).sub(map.tiles.length * .5f, map.tiles[0].length * .5f).len();
        float diag = (float) (Math.sqrt(map.tiles.length * map.tiles.length + map.tiles[0].length * map.tiles[0].length) * .5f);

        dist = 1 - dist / diag;

        return noise.get(x, y, elevation) * dist;
    }
}
