package mystiqa.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Assets;
import mystiqa.Perlin;
import mystiqa.Voronoi;
import mystiqa.entity.tile.Tile;

import java.util.HashMap;

public class WorldGenerator {
    public int waterLevel;

    private Array<Biome> biomes;
    private Perlin elevationNoise;
    private Perlin terrainNoise;

    public WorldGenerator() {
        waterLevel = 0;

        biomes = new Array<Biome>();

        elevationNoise = new Perlin(.002f, 8);
        terrainNoise = new Perlin(.0075f, 8);
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
        int _height = getHeight(x, y);

        if (z <= _height) {
            return Assets.getInstance().getTile("Grass");
        } else if (z <= waterLevel) {
            return Assets.getInstance().getTile("Water");
        }

        return null;
    }

    public float getElevation(int x, int y) {
        return MathUtils.clamp(elevationNoise.get(x, y), 0, 1);
    }

    public int getHeight(int x, int y) {
        return (int) (waterLevel + MathUtils.lerp(-8, 16, terrainNoise.get(x, y)));
    }

    public void deserialize(JsonValue json) {
        if (json.has("waterLevel")) {
            waterLevel = json.getInt("waterLevel");
        }

        if (json.has("biomes")) {
            for (JsonValue biome : json.get("biomes")) {
                biomes.add(Assets.getInstance().getBiome(biome.asString()));
            }
        }
    }
}
