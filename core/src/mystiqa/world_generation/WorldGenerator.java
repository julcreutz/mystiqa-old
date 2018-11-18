package mystiqa.world_generation;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import mystiqa.Perlin;
import mystiqa.Resources;
import mystiqa.entity.tile.Chunk;
import mystiqa.entity.tile.Tile;
import mystiqa.world_generation.biome.Biome;
import mystiqa.world_generation.biome.Grassland;
import mystiqa.world_generation.biome.Hills;
import mystiqa.world_generation.biome.Mountains;

import java.util.HashMap;

public class WorldGenerator {
    public static final int WATER_LEVEL = 64;

    private Perlin perlin;
    private Array<Biome> biomes;

    public WorldGenerator() {
        perlin = new Perlin();
        biomes = new Array<Biome>();

        biomes.add(new Grassland());
        biomes.add(new Hills());
        biomes.add(new Mountains());
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
        int height = getHeight(x, y);

        if (height < WATER_LEVEL) {
            if (z > height && z <= WATER_LEVEL) {
                return Resources.getInstance().getTile("Water");
            } else if (z <= height) {
                return b.getWaterTile();
            }
        } else {
            if (z <= height) {
                return b.getGroundTile();
            }
        }

        return null;
    }

    public HashMap<Biome, Float> getBiomes(int x, int y) {
        HashMap<Biome, Float> biomes = new HashMap<Biome, Float>();

        float elevation = getElevation(x, y);
        if (elevation > 1) {
            System.out.println(elevation);
        }

        for (Biome b : this.biomes) {
            biomes.put(b, 1 - Math.abs(b.targetElevation - elevation));
        }

        return biomes;
    }

    public int getHeight(int x, int y) {
        float elevation = 0;

        HashMap<Biome, Float> biomes = getBiomes(x, y);

        for (Biome b : biomes.keySet()) {
            elevation += b.getHeight(x, y) * biomes.get(b);
        }

        float _elevation = getElevation(x, y);

        return (int) (WATER_LEVEL + getBiome(x, y).getHeight(x, y));
    }

    public float getElevation(int x, int y) {
        return perlin.layeredNoise(x, y, .0075f, 4, 1);
    }

    public Biome getBiome(int x, int y) {
        HashMap<Biome, Float> biomes = getBiomes(x, y);

        Biome biome = null;
        float max = 0;

        for (Biome b : biomes.keySet()) {
            float curr = biomes.get(b);

            if (curr >= max) {
                max = curr;
                biome = b;
            }
        }

        return biome;
    }
}
