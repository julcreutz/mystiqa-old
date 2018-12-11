package game.main.world_map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.loader.*;
import game.main.world_map.biome.WorldMapBiome;
import game.main.world_map.biome.WorldMapBiomeTile;
import game.main.world_map.entity.WorldMapEntity;
import game.main.world_map.entity.WorldMapPlayer;
import game.main.world_map.site.WorldMapSite;
import game.main.world_map.site.WorldMapSiteType;
import game.main.world_map.tile.WorldMapTile;
import game.main.world_map.tile.WorldMapTileType;
import game.noise.Noise;
import game.noise.NoiseParameters;

import java.util.Random;

public class WorldMapGenerator {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;

    public static final float FALL_OFF = .75f;

    public static final NoiseParameters ELEVATION = new NoiseParameters(3, .05f, 1);
    public static final NoiseParameters TEMPERATURE = new NoiseParameters(6, .05f, 1);

    public static void generate(WorldMap map) {
        Random rand = new Random(MathUtils.random(Long.MAX_VALUE));

        Noise elevation = new Noise(rand.nextLong());
        Noise temperature = new Noise(rand.nextLong());

        map.tiles = new WorldMapTile[WIDTH][HEIGHT];

        for (int x = 0; x < map.tiles.length; x++) {
            for (int y = 0; y < map.tiles[0].length; y++) {
                WorldMapBiome biome = getBiome(map, elevation, temperature, x, y);

                float e = getElevation(elevation, ELEVATION, map, x, y);
                float t = getTemperature(temperature, TEMPERATURE, map, x, y);

                WorldMapTileType type = null;

                for (WorldMapBiomeTile tile : biome.tiles) {
                    if (e >= tile.minElevation && e <= tile.maxElevation && t >= tile.minTemperature && t <= tile.maxTemperature) {
                        type = tile.type;
                    }
                }

                map.tiles[x][y] = new WorldMapTile(type, x, y);
            }
        }

        for (int x = 0; x < map.tiles.length; x++) {
            for (int y = 0; y < map.tiles[0].length; y++) {
                if (rand.nextFloat() <= getBiome(map, elevation, temperature, x, y).riverDensity) {
                    int rx = x;
                    int ry = y;

                    while (map.tiles[rx][ry] != null && !map.tiles[rx][ry].type.name.equals("Water")) {
                        map.tiles[rx][ry].type = WorldMapTileTypeLoader.load("River");

                        float low = getElevation(elevation, ELEVATION, map, rx, ry);

                        int _x = 0;
                        int _y = 0;

                        for (int xx = -1; xx <= 1; xx++) {
                            for (int yy = -1; yy <= 1; yy++) {
                                if ((xx != 0 || yy != 0) && xx * yy == 0 && map.tiles[rx][ry] != null) {
                                    float e = getElevation(elevation, ELEVATION, map, rx + xx, ry + yy);

                                    if (e < low) {
                                        low = e;

                                        _x = rx + xx;
                                        _y = ry + yy;
                                    }
                                }
                            }
                        }

                        rx = _x;
                        ry = _y;
                    }
                }
            }
        }

        map.sites = new WorldMapSite[map.tiles.length][map.tiles[0].length];

        for (int i = 0; i < 1; i++) {
            int x;
            int y;

            do {
                x = rand.nextInt(map.tiles.length);
                y = rand.nextInt(map.tiles[0].length);
            } while (map.tiles[x][y] != null && !map.tiles[x][y].type.name.equals("Grass"));

            WorldMapSiteType type = new WorldMapSiteType();
            type.sheet = SheetLoader.load("House");
            type.color = ColorLoader.load("Brown");
            WorldMapSite site = new WorldMapSite(type, x, y);

            map.sites[x][y] = site;
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

    public static float getTemperature(Noise noise, NoiseParameters temperature, WorldMap map, int x, int y) {
        return (1 - (float) Math.pow(Math.abs(y - map.tiles[0].length / 2) / (map.tiles[0].length * .5f), .25f) * noise.get(x, y, temperature));
    }

    public static WorldMapBiome getBiome(WorldMap map, Noise elevation, Noise temperature, int x, int y) {
        float e = getElevation(elevation, ELEVATION, map, x, y);
        float t = getTemperature(temperature, TEMPERATURE, map, x, y);

        WorldMapBiome biome = null;

        for (WorldMapBiome _biome : WorldMapBiomeLoader.loadAll()) {
            if (e >= _biome.minElevation && e <= _biome.maxElevation && t >= _biome.minTemperature && t <= _biome.maxTemperature) {
                biome = _biome;
            }
        }

        return biome;
    }
}
