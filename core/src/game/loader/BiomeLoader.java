package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import game.main.gen.Biome;

import java.util.HashMap;

public class BiomeLoader {
    private static HashMap<String, Biome> biomes;

    public static void load() {
        biomes = new HashMap<>();

        for (JsonValue json : new JsonReader().parse(Gdx.files.internal("data/biomes.json"))) {
            Biome b = new Biome();

            b.deserialize(json);

            biomes.put(json.name, b);
        }
    }

    public static Biome load(String name) {
        return biomes.getOrDefault(name, null);
    }

    public static Array<Biome> loadAll() {
        Array<Biome> biomes = new Array<>();

        for (Biome b : BiomeLoader.biomes.values()) {
            biomes.add(b);
        }

        return biomes;
    }
}
