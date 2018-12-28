package game.loader.resource;

import game.main.gen.Biome;

public class BiomeLoader extends ResourceLoader<Biome> {
    @Override
    public Biome newInstance() {
        return new Biome();
    }
}
