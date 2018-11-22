package mystiqa.world;

import com.badlogic.gdx.utils.Array;
import mystiqa.Assets;
import mystiqa.Perlin;
import mystiqa.entity.tile.Tile;

public class WorldGenerator {
    public int waterLevel;

    public Array<Terrain> possibleTerrains;
    public Array<Climate> possibleClimates;

    public Perlin elevationNoise;
    public Perlin temperatureNoise;

    public WorldGenerator() {
        possibleTerrains = new Array<Terrain>();
        possibleClimates = new Array<Climate>();

        elevationNoise = new Perlin(.0075f, 4);
        temperatureNoise = new Perlin(.00375f, 2);
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
        Climate climate = getClimate(x, y);
        int height = getHeight(x, y);

        if (z <= waterLevel) {
            if (z <= height) {
                return Assets.getInstance().getTile(climate.underWaterTile);
            } else {
                return Assets.getInstance().getTile(climate.waterTile);
            }
        } else {
            if (z <= height) {
                return Assets.getInstance().getTile(climate.aboveWaterTile);
            }
        }

        return null;
    }

    public int getHeight(int x, int y) {
        return waterLevel + getTerrain(x, y).getHeight(x, y);
    }

    public Terrain getTerrain(int x, int y) {
        float elevation = elevationNoise.get(x, y);

        Terrain terrain = null;
        float best = 1;

        for (Terrain _terrain : possibleTerrains) {
            float _elevation = Math.abs(_terrain.targetElevation - elevation);

            if (_elevation <= best) {
                terrain = _terrain;
                best = _elevation;
            }
        }

        return terrain;
    }

    public Climate getClimate(int x, int y) {
        float temperature = temperatureNoise.get(x, y);

        Climate climate = null;
        float best = 1;

        for (Climate _climate : possibleClimates) {
            float _temperature = Math.abs(_climate.targetTemperature - temperature);

            if (_temperature <= best) {
                climate = _climate;
                best = _temperature;
            }
        }

        return climate;
    }
}
