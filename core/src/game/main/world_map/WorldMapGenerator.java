package game.main.world_map;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.loader.*;
import game.main.world_map.biome.Biome;
import game.main.world_map.entity.WorldMapEntity;
import game.main.world_map.entity.WorldMapPlayer;
import game.main.world_map.site.Site;
import game.main.world_map.site.SiteType;
import game.main.world_map.biome.BiomeType;
import game.noise.Noise;
import game.noise.NoiseParameters;

import java.util.Random;

public class WorldMapGenerator {
    public static final int WIDTH = 128;
    public static final int HEIGHT = 128;

    public static final NoiseParameters ELEVATION = new NoiseParameters(6, 0.03125f, 1);

    public WorldMapState map;

    public Random rand;

    public Noise elevation;

    public WorldMapGenerator(WorldMapState map) {
        this.map = map;
    }

    public void generate() {
        rand = new Random(MathUtils.random(Long.MAX_VALUE));

        elevation = new Noise(rand.nextLong());

        map.biomes = new Biome[WIDTH][HEIGHT];

        for (int x = 0; x < map.biomes.length; x++) {
            for (int y = 0; y < map.biomes[0].length; y++) {
                map.biomes[x][y] = new Biome(biomeAt(x, y), x, y);
            }
        }

        for (int x = 0; x < map.biomes.length; x++) {
            for (int y = 0; y < map.biomes[0].length; y++) {
                if (!biomeAt(x, y).name.equals("Mountains")) continue;

                boolean highest = true;

                for (int xx = -1; xx <= 1; xx++) {
                    for (int yy = -1; yy <= 1; yy++) {
                        if (xx != 0 || yy != 0) {
                            int xxx = x + xx;
                            int yyy = y + yy;

                            if (xxx >= 0 && xxx < map.biomes.length && yyy >= 0 && yyy < map.biomes[0].length) {
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

                    while (rx >= 0 && rx < map.biomes.length && ry >= 0 && ry < map.biomes[0].length && map.biomes[rx][ry] != null && !map.biomes[rx][ry].type.name.equals("Water")) {
                        map.biomes[rx][ry].type = BiomeLoader.load("River");

                        float low = elevationAt(rx, ry);

                        int _x = 0;
                        int _y = 0;

                        for (int xx = -1; xx <= 1; xx++) {
                            for (int yy = -1; yy <= 1; yy++) {
                                if ((xx != 0 || yy != 0) && xx * yy == 0 && map.biomes[rx][ry] != null) {
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

        map.sites = new Site[map.biomes.length][map.biomes[0].length];

        for (int i = 0; i < 1; i++) {
            int x;
            int y;

            do {
                x = rand.nextInt(map.biomes.length);
                y = rand.nextInt(map.biomes[0].length);
            } while (map.biomes[x][y] != null && !map.biomes[x][y].type.name.equals("Grass"));

            SiteType type = new SiteType();
            type.sheet = SheetLoader.load("House");
            type.color = ColorLoader.load("Brown");
            Site site = new Site(type, x, y);

            map.sites[x][y] = site;
        }

        map.entities = new Array<WorldMapEntity>();

        int x;
        int y;

        do {
            x = rand.nextInt(map.biomes.length);
            y = rand.nextInt(map.biomes[0].length);
        } while (map.biomes[x][y] != null && !map.biomes[x][y].type.name.equals("Grass"));

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

    public BiomeType biomeAt(int x, int y) {
        float e = elevationAt(x, y);

        for (BiomeType biome : BiomeLoader.loadAll()) {
            if (e >= biome.minElevation && e <= biome.maxElevation) {
                return biome;
            }
        }

        return null;
    }

    public float elevationAt(int x, int y) {
        return elevation.noiseAt(x, y, ELEVATION) * MathUtils.clamp((1 - new Vector2(map.biomes.length * .5f, map.biomes[0].length * .5f)
                .sub(x, y).len() / ((float) Math.sqrt(map.biomes.length * map.biomes.length + map.biomes[0].length * map.biomes[0].length) * .5f)) + .25f, 0, 1);
    }
}
