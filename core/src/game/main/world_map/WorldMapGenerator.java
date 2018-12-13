package game.main.world_map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.loader.*;
import game.main.world_map.biome.WorldMapBiome;
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
    public static final int WIDTH = 128;
    public static final int HEIGHT = 128;

    public static final NoiseParameters ELEVATION = new NoiseParameters(6, 0.03125f, 1);

    public WorldMap map;

    public Random rand;

    public Noise elevation;

    public WorldMapGenerator(WorldMap map) {
        this.map = map;
    }

    public void generate() {
        rand = new Random(MathUtils.random(Long.MAX_VALUE));

        elevation = new Noise(rand.nextLong());

        map.tiles = new WorldMapTile[WIDTH][HEIGHT];

        for (int x = 0; x < map.tiles.length; x++) {
            for (int y = 0; y < map.tiles[0].length; y++) {
                map.tiles[x][y] = new WorldMapTile(tileAt(x, y), x, y);
            }
        }

        for (int x = 0; x < map.tiles.length; x++) {
            for (int y = 0; y < map.tiles[0].length; y++) {
                if (!tileAt(x, y).name.equals("Mountains")) continue;

                boolean highest = true;

                for (int xx = -1; xx <= 1; xx++) {
                    for (int yy = -1; yy <= 1; yy++) {
                        if (xx != 0 || yy != 0) {
                            int xxx = x + xx;
                            int yyy = y + yy;

                            if (xxx >= 0 && xxx < map.tiles.length && yyy >= 0 && yyy < map.tiles[0].length) {
                                if (elevationAt(xxx, yyy) > elevationAt(x, y)) {
                                    highest = false;
                                }
                            }
                        }
                    }
                }

                if (highest && rand.nextFloat() < .25f) {
                    int rx = x;
                    int ry = y;

                    while (rx >= 0 && rx < map.tiles.length && ry >= 0 && ry < map.tiles[0].length && map.tiles[rx][ry] != null && !map.tiles[rx][ry].type.name.equals("Water")) {
                        map.tiles[rx][ry].type = WorldMapTileTypeLoader.load("River");

                        float low = elevationAt(rx, ry);

                        int _x = 0;
                        int _y = 0;

                        for (int xx = -1; xx <= 1; xx++) {
                            for (int yy = -1; yy <= 1; yy++) {
                                if ((xx != 0 || yy != 0) && xx * yy == 0 && map.tiles[rx][ry] != null) {
                                    float e = elevationAt(rx + xx, ry + yy);

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

        map.lastX = map.nextX = x;
        map.lastY = map.nextY = y;

        map.cursorX = x;
        map.cursorY = y;

        map.cam.position.x = x * 8 + 4;
        map.cam.position.y = y * 8 + 4;
    }

    public WorldMapBiome biomeAt(int x, int y) {
        float e = elevationAt(x, y);

        for (WorldMapBiome biome : WorldMapBiomeLoader.loadAll()) {
            if (e >= biome.minElevation && e <= biome.maxElevation) {
                return biome;
            }
        }

        return null;
    }

    public WorldMapTileType tileAt(int x, int y) {
        return biomeAt(x, y).type;
    }

    public float elevationAt(int x, int y) {
        return elevation.get(x, y, ELEVATION) * MathUtils.clamp((1 - new Vector2(map.tiles.length * .5f, map.tiles[0].length * .5f)
                .sub(x, y).len() / ((float) Math.sqrt(map.tiles.length * map.tiles.length + map.tiles[0].length * map.tiles[0].length) * .5f)) + .25f, 0, 1);
    }
}
