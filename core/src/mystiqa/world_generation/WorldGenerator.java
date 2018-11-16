package mystiqa.world_generation;

import mystiqa.Perlin;
import mystiqa.Resources;
import mystiqa.entity.tile.Chunk;
import mystiqa.entity.tile.Tile;
import mystiqa.world_generation.biome.Biome;
import mystiqa.world_generation.biome.Grassland;
import mystiqa.world_generation.biome.Hills;

public class WorldGenerator {
    public static final int WATER_LEVEL = 64;

    private Perlin perlin;
    private Grassland grassland;
    private Hills hills;

    public WorldGenerator() {
        perlin = new Perlin();
        grassland = new Grassland();
        hills = new Hills();
    }

    public void generateChunk(Chunk c) {
        for (int x = 0; x < c.tiles.length; x++) {
            for (int y = 0; y < c.tiles[0].length; y++) {
                for (int z = 0; z < getHeight(c.x + x, c.y + y); z++) {
                    c.setTile(get(c.x + x, c.y + y, z), x, y, z);
                }
            }
        }

        for (int x = 0; x < c.tiles.length; x++) {
            for (int y = 0; y < c.tiles[0].length; y++) {
                for (int z = 0; z < WATER_LEVEL; z++) {
                    if (c.getTile(x, y, z) == null) {
                        c.setTile(Resources.getTile("Water"), x, y, z);
                    }
                }
            }
        }
    }

    public Tile get(int x, int y, int z) {
        Biome b = getBiome(x, y);
        int height = b.getHeight(x, y);

        if (z <= WATER_LEVEL) {
            return b.getWaterTile();
        } else {
            return b.getGroundTile();
        }
    }

    public int getHeight(int x, int y) {
        int originalHeight = getBiome(x, y).getHeight(x, y);
        int height = 0;
        int n = 0;
        for (int xx = -1; xx <= 1; xx++) {
            for (int yy = -1; yy <= 1; yy++) {
                height += getBiome(x + xx, y + yy).getHeight(x + xx, y + yy);
                n++;
            }
        }

        float p = (float) height / (float) originalHeight;
        float factor = .5f;

        if (p > 1 + factor || p < 1 - factor) {
            return height / n;
        } else {
            return originalHeight;
        }
    }

    public Biome getBiome(int x, int y) {
        return perlin.layeredNoise(x, y, 4, .0075f, 4, 1, 1 / 4f) > .55f ? hills : grassland;
    }
}
