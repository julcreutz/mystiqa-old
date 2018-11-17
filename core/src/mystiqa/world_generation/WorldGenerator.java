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
                for (int z = 0; z < c.tiles[0][0].length; z++) {
                    Tile t = get(c.x + x, c.y + y, c.z + z);

                    if (t != null) {
                        c.setTile(t, x, y, z);
                    }
                }
            }
        }
    }

    public Tile get(int x, int y, int z) {
        Biome b = getBiome(x, y);
        int height = b.getHeight(x, y);

        if (z <= WATER_LEVEL) {
            if (z > height) {
                return Resources.getInstance().getTile("Water");
            } else {
                return b.getWaterTile();
            }
        } else {
            if (z <= height) {
                return b.getGroundTile();
            }
        }

        return null;
    }

    public int getHeight(int x, int y) {
        return getBiome(x, y).getHeight(x, y);
    }

    public Biome getBiome(int x, int y) {
        return perlin.layeredNoise(x, y, 4, .0075f, 4, 1, 1 / 4f) > .55f ? hills : grassland;
    }
}
