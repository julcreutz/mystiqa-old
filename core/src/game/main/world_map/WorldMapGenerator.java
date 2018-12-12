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

    public static final int ISLAND_WIDTH = 16;
    public static final int ISLAND_HEIGHT = 16;

    public static final NoiseParameters ELEVATION = new NoiseParameters(3, .2f, 1);

    public static void generate(WorldMap map) {
        Random rand = new Random(MathUtils.random(Long.MAX_VALUE));

        Noise elevation = new Noise(rand.nextLong());

        WorldMapBiome[][] biomes = new WorldMapBiome[WIDTH / ISLAND_WIDTH][HEIGHT / ISLAND_HEIGHT];

        for (int x = 0; x < biomes.length; x++) {
            for (int y = 0; y < biomes[0].length; y++) {
                biomes[x][y] = WorldMapBiomeLoader.loadAll().random();
            }
        }

        map.tiles = new WorldMapTile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH / ISLAND_WIDTH; x++) {
            for (int y = 0; y < HEIGHT / ISLAND_HEIGHT; y++) {
                for (int xx = 0; xx < ISLAND_WIDTH; xx++) {
                    for (int yy = 0; yy < ISLAND_HEIGHT; yy++) {
                        int xxx = x * ISLAND_WIDTH + xx;
                        int yyy = y * ISLAND_HEIGHT + yy;

                        map.tiles[xxx][yyy] = new WorldMapTile(getBiomeTile(biomes, elevation, xxx, yyy).type, xxx, yyy);
                    }
                }
            }
        }

        for (int x = 0; x < map.tiles.length; x++) {
            for (int y = 0; y < map.tiles[0].length; y++) {
                if (rand.nextFloat() <= getBiomeTile(biomes, elevation, x, y).riverDensity) {
                    int rx = x;
                    int ry = y;

                    while (rx >= 0 && rx < map.tiles.length && ry >= 0 && ry < map.tiles[0].length && map.tiles[rx][ry] != null && !map.tiles[rx][ry].type.name.equals("Water")) {
                        map.tiles[rx][ry].type = WorldMapTileTypeLoader.load("River");

                        float low = getElevation(elevation, ELEVATION, rx, ry);

                        int _x = 0;
                        int _y = 0;

                        for (int xx = -1; xx <= 1; xx++) {
                            for (int yy = -1; yy <= 1; yy++) {
                                if ((xx != 0 || yy != 0) && xx * yy == 0 && map.tiles[rx][ry] != null) {
                                    float e = getElevation(elevation, ELEVATION, rx + xx, ry + yy);

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

    public static float getElevation(Noise noise, NoiseParameters elevation, int x, int y) {
        float dist = MathUtils.clamp(1 - new Vector2(x % ISLAND_WIDTH, y % ISLAND_HEIGHT).sub(ISLAND_WIDTH * .5f, ISLAND_HEIGHT * .5f).len() / (float) (Math.sqrt(ISLAND_WIDTH * ISLAND_WIDTH + ISLAND_HEIGHT * ISLAND_HEIGHT) * .5f), 0, 1);
        return MathUtils.clamp(noise.get(x, y, elevation), 0, 1) * dist;
    }

    public static float getTemperature(Noise noise, NoiseParameters temperature, WorldMap map, int x, int y) {
        //return MathUtils.clamp((1 - (Math.abs(y - map.tiles[0].length * .5f) / (map.tiles[0].length * .5f))) + (-1f + noise.get(x, y, temperature) * 2f) * .5f, 0, 1);
        return noise.get(x, y, temperature);
    }

    public static WorldMapBiome getBiome(WorldMapBiome[][] biomes, int x, int y) {
        return biomes[MathUtils.floor(x / ISLAND_WIDTH)][MathUtils.floor(y / ISLAND_HEIGHT)];
    }

    public static WorldMapBiomeTile getBiomeTile(WorldMapBiome[][] biomes, Noise elevation, int x, int y) {
        WorldMapBiome biome = getBiome(biomes, x, y);

        float e = getElevation(elevation, ELEVATION, x, y);

        for (WorldMapBiomeTile tile : biome.tiles) {
            if (e >= tile.minElevation && e <= tile.maxElevation) {
                return tile;
            }
        }

        return null;
    }
}
