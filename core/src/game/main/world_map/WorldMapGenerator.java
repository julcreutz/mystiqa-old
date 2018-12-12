package game.main.world_map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
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

import java.util.HashMap;
import java.util.Random;

public class WorldMapGenerator {
    public static final int WIDTH = 128;
    public static final int HEIGHT = 128;

    public static final int ISLAND_WIDTH = 16;
    public static final int ISLAND_HEIGHT = 16;

    public WorldMap map;

    public Random rand;

    public Array<Rectangle> islands;
    public HashMap<Rectangle, WorldMapBiome> biomes;

    public Noise elevation;

    public WorldMapGenerator(WorldMap map) {
        this.map = map;
    }

    public void generate() {
        rand = new Random(MathUtils.random(Long.MAX_VALUE));

        islands = new Array<Rectangle>();

        while (true) {
            boolean full = true;

            for (int x = 0; x < WIDTH / ISLAND_WIDTH; x++) {
                for (int y = 0; y < HEIGHT / ISLAND_HEIGHT; y++) {
                    if (getIsland(x, y) == null) {
                        full = false;
                    }
                }
            }

            if (full) {
                break;
            }

            Rectangle island = new Rectangle();

            while (true) {
                island.width = 4;
                island.height = 4;

                if (rand.nextFloat() < .5f) {
                    island.width = 3;
                    island.height = 3;

                    if (rand.nextFloat() < .5f) {
                        island.width = 2;
                        island.height = 2;

                        if (rand.nextFloat() < .5f) {
                            island.width = 1;
                            island.height = 1;
                        }
                    }
                }

                island.x = rand.nextInt(WIDTH / ISLAND_WIDTH - (int) island.width + 1);
                island.y = rand.nextInt(HEIGHT / ISLAND_HEIGHT - (int) island.height + 1);

                boolean free = true;

                for (int x = 0; x < island.width; x++) {
                    for (int y = 0; y < island.height; y++) {
                        if (getIsland((int) island.x + x, (int) island.y + y) != null) {
                            free = false;
                        }
                    }
                }

                if (free) {
                    islands.add(island);
                    break;
                }
            }
        }

        elevation = new Noise(rand.nextLong());

        biomes = new HashMap<Rectangle, WorldMapBiome>();

        for (Rectangle island : islands) {
            Array<WorldMapBiome> biomes = WorldMapBiomeLoader.loadAll();
            this.biomes.put(island, biomes.get(rand.nextInt(biomes.size)));
        }

        map.tiles = new WorldMapTile[WIDTH][HEIGHT];

        for (Rectangle island : islands) {
            int x1 = (int) island.x * ISLAND_WIDTH;
            int x2 = (int) (island.x + island.width) * ISLAND_WIDTH;

            int y1 = (int) island.y * ISLAND_HEIGHT;
            int y2 = (int) (island.y + island.height) * ISLAND_HEIGHT;

            for (int x = x1; x < x2; x++) {
                for (int y = y1; y < y2; y++) {
                    map.tiles[x][y] = new WorldMapTile(getBiomeTile(x, y).type, x, y);
                }
            }
        }

        for (int x = 0; x < map.tiles.length; x++) {
            for (int y = 0; y < map.tiles[0].length; y++) {
                if (rand.nextFloat() <= getBiomeTile(x, y).riverDensity) {
                    int rx = x;
                    int ry = y;

                    while (rx >= 0 && rx < map.tiles.length && ry >= 0 && ry < map.tiles[0].length && map.tiles[rx][ry] != null && !map.tiles[rx][ry].type.name.equals("Water")) {
                        map.tiles[rx][ry].type = WorldMapTileTypeLoader.load("River");

                        float low = getElevation(rx, ry);

                        int _x = 0;
                        int _y = 0;

                        for (int xx = -1; xx <= 1; xx++) {
                            for (int yy = -1; yy <= 1; yy++) {
                                if ((xx != 0 || yy != 0) && xx * yy == 0 && map.tiles[rx][ry] != null) {
                                    float e = getElevation(rx + xx, ry + yy);

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

    public WorldMapBiomeTile getBiomeTile(int x, int y) {
        float e = getElevation(x, y);

        for (WorldMapBiomeTile tile : biomes.get(getIsland(x / ISLAND_WIDTH, y / ISLAND_HEIGHT)).tiles) {
            if (e >= tile.minElevation && e <= tile.maxElevation) {
                return tile;
            }
        }

        return null;
    }

    public float getElevation(int x, int y) {
        Rectangle island = getIsland(x / ISLAND_WIDTH, y / ISLAND_HEIGHT);

        return elevation.get(x, y, new NoiseParameters(3, .1f / ((island.width * ISLAND_WIDTH) / ISLAND_WIDTH), 1))
                * (1 - new Vector2((island.x + island.width * .5f) * ISLAND_WIDTH, (island.y + island.height * .5f) * ISLAND_HEIGHT)
                .sub(x, y).len() / ((float) Math.sqrt(island.width * ISLAND_WIDTH * island.width * ISLAND_WIDTH + island.height * ISLAND_HEIGHT * island.height * ISLAND_HEIGHT) * .5f));
    }

    public Rectangle getIsland(int x, int y) {
        for (Rectangle island : islands) {
            if (x >= island.x && x < island.x + island.width && y >= island.y && y < island.y + island.height) {
                return island;
            }
        }

        return null;
    }
}
