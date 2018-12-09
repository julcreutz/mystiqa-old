package game.main.world_map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.loader.WorldMapPlayerTypeLoader;
import game.loader.WorldMapTileTypeLoader;
import game.main.world_map.entity.WorldMapEntity;
import game.main.world_map.entity.WorldMapPlayer;
import game.main.world_map.tile.WorldMapTile;
import game.main.world_map.tile.WorldMapTileType;
import game.noise.Noise;
import game.noise.NoiseParameters;

import java.util.Random;

public class WorldMapGenerator {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    public static final float FALL_OFF = .75f;

    public static final int MIN_RIVERS = 3;
    public static final int MAX_RIVERS = 7;

    public static final NoiseParameters ELEVATION = new NoiseParameters(3, .05f, 1);
    public static final NoiseParameters TEMPERATURE = new NoiseParameters(6, .05f, 1);

    public static void generate(WorldMap map) {
        Random rand = new Random(MathUtils.random(Long.MAX_VALUE));

        Noise elevation = new Noise(rand.nextLong());
        Noise temperature = new Noise(rand.nextLong());

        map.tiles = new WorldMapTile[WIDTH][HEIGHT];

        for (int x = 0; x < map.tiles.length; x++) {
            for (int y = 0; y < map.tiles[0].length; y++) {
                float e = getElevation(elevation, ELEVATION, map, x, y);
                float t = temperature.get(x, y, TEMPERATURE);

                WorldMapTileType type;

                if (e > .325f) {
                    if (t > .6f) {
                        type = WorldMapTileTypeLoader.load("Sand");
                    } else {
                        if (e > .55f) {
                            type = WorldMapTileTypeLoader.load("Mountains");
                        } else if (e > .475f) {
                            type = WorldMapTileTypeLoader.load("Hills");
                        } else if (e > .4f) {
                            type = WorldMapTileTypeLoader.load("Tree");
                        } else if (e > .35f) {
                            type = WorldMapTileTypeLoader.load("Grass");
                        } else {
                            type = WorldMapTileTypeLoader.load("Sand");
                        }
                    }
                } else {
                    type = WorldMapTileTypeLoader.load("Water");
                }

                map.tiles[x][y] = new WorldMapTile(type, x, y);
            }
        }

        for (int i = 0; i < MIN_RIVERS + rand.nextInt(MAX_RIVERS - MIN_RIVERS + 1); i++) {
            int x;
            int y;

            int steps = 0;

            do {
                x = rand.nextInt(map.tiles.length);
                y = rand.nextInt(map.tiles[0].length);

                steps++;
            } while (map.tiles[x][y] != null && !map.tiles[x][y].type.name.equals(steps > 150 ? "Grass" : steps > 100 ? "Tree" : (steps > 50 ? "Hills" : "Mountains")));

            while (map.tiles[x][y] != null && !map.tiles[x][y].type.name.equals("Water")) {
                map.tiles[x][y].type = WorldMapTileTypeLoader.load("River");

                float low = getElevation(elevation, ELEVATION, map, x, y);

                int _x = 0;
                int _y = 0;

                for (int xx = -1; xx <= 1; xx++) {
                    for (int yy = -1; yy <= 1; yy++) {
                        if ((xx != 0 || yy != 0) && xx * yy == 0 && map.tiles[x][y] != null) {
                            float e = getElevation(elevation, ELEVATION, map, x + xx, y + yy);

                            if (e < low) {
                                low = e;

                                _x = x + xx;
                                _y = y + yy;
                            }
                        }
                    }
                }

                x = _x;
                y = _y;
            }
        }

        map.entities = new Array<WorldMapEntity>();

        int x;
        int y;

        do {
            x = rand.nextInt(map.tiles.length);
            y = rand.nextInt(map.tiles[0].length);
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
        return noise.get(x, y, elevation) * (float) Math.pow((1 - (new Vector2(x, y).sub(map.tiles.length * .5f, map.tiles[0].length * .5f).len() / (float) (Math.sqrt(map.tiles.length * map.tiles.length + map.tiles[0].length * map.tiles[0].length) * .5f))), FALL_OFF);
    }
}
