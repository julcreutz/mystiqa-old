package game.loader.resource;

import game.main.state.play.map.world.Biome;

public class BiomeLoader extends ResourceLoader<Biome> {
    @Override
    public Biome newInstance() {
        return new Biome();
    }
}
