package game.main.world_map.biome;

import com.badlogic.gdx.utils.Array;

public class WorldMapBiome {
    public float minElevation;
    public float maxElevation;

    public Array<WorldMapBiomeTile> tiles;

    public WorldMapBiome() {
        tiles = new Array<WorldMapBiomeTile>();
    }
}
