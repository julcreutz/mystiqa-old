package mystiqa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.entity.actor.Actor;
import mystiqa.entity.actor.humanoid.Humanoid;
import mystiqa.entity.actor.humanoid.race.HumanoidRace;
import mystiqa.entity.actor.slime.Slime;
import mystiqa.entity.tile.Tile;
import mystiqa.entity.tile.TileType;
import mystiqa.world.biome.Biome;
import mystiqa.world.structure.Structure;

import java.util.HashMap;

public final class Assets {
    public static final String HUMANOID_RACES = "data/humanoid_races/";
    public static final String BEINGS = "data/entities/actors/";

    private static Array<Texture> textures;
    private static HashMap<String, TextureRegion[][]> spriteSheets;

    private static HashMap<String, Color> colors;

    private static HashMap<String, Biome> biomes;
    private static HashMap<String, Structure> structures;

    private static HashMap<String, HumanoidRace> humanoidRaces;

    private static HashMap<String, TileType> tiles;

    private static HashMap<String, JsonValue> actors;

    private Assets() {

    }

    public static TextureRegion[][] getSpriteSheet(String id) {
        if (textures == null && spriteSheets == null) {
            textures = new Array<Texture>();
            spriteSheets = new HashMap<String, TextureRegion[][]>();

            for (JsonValue spriteSheet : new JsonReader().parse(Gdx.files.internal("data/sprite_sheets.json")).get("spriteSheets")) {
                Texture t = new Texture(Gdx.files.internal(spriteSheet.getString("path")));
                textures.add(t);

                int splitX = spriteSheet.getInt("splitX");
                int splitY = spriteSheet.getInt("splitY");

                int w = t.getWidth() / splitX;
                int h = t.getHeight() / splitY;

                TextureRegion[][] _spriteSheet = new TextureRegion[w][h];
                for (int x = 0; x < _spriteSheet.length; x++) {
                    for (int y = 0; y < _spriteSheet[0].length; y++) {
                        _spriteSheet[x][y] = new TextureRegion(t, x * splitX, y * splitY, splitX, splitY);
                    }
                }

                spriteSheets.put(spriteSheet.getString("id"), _spriteSheet);
            }
        }

        return spriteSheets.get(id);
    }

    public static Color getColor(String id) {
        if (colors == null) {
            colors = new HashMap<String, Color>();

            for (JsonValue color : new JsonReader().parse(Gdx.files.internal("data/colors.json")).get("colors")) {
                colors.put(color.getString("id"), new Color(color.getInt("r") / 255f, color.getInt("g") / 255f, color.getInt("b") / 255f, 1));
            }
        }

        return colors.get(id);
    }

    public static Biome getBiome(String id) {
        if (biomes == null) {
            biomes = new HashMap<String, Biome>();

            for (JsonValue biome : new JsonReader().parse(Gdx.files.internal("data/biomes.json")).get("biomes")) {
                Biome b = new Biome();
                b.deserialize(biome);

                biomes.put(biome.getString("id"), b);
            }
        }

        return biomes.get(id);
    }

    public static Structure getStructure(String id) {
        if (structures == null) {
            structures = new HashMap<String, Structure>();

            for (JsonValue structure : new JsonReader().parse(Gdx.files.internal("data/structures.json")).get("structures")) {
                Structure _structure = new Structure();
                _structure.deserialize(structure);

                structures.put(structure.getString("id"), _structure);
            }
        }

        return structures.get(id);
    }

    public static HumanoidRace getHumanoidRace(String id) {
        if (humanoidRaces == null) {
            humanoidRaces = new HashMap<String, HumanoidRace>();

            for (JsonValue humanoidRace : new JsonReader().parse(Gdx.files.internal("data/humanoid_races.json")).get("humanoid_races")) {
                HumanoidRace _humanoidRace = new HumanoidRace();
                _humanoidRace.deserialize(humanoidRace);

                humanoidRaces.put(humanoidRace.getString("id"), _humanoidRace);
            }
        }

        return humanoidRaces.get(id);
    }

    public static Tile getTile(String id) {
        if (tiles == null) {
            tiles = new HashMap<String, TileType>();

            for (JsonValue tile : new JsonReader().parse(Gdx.files.internal("data/tiles.json")).get("tiles")) {
                TileType t = new TileType();
                t.deserialize(tile);

                tiles.put(tile.getString("id"), t);
            }
        }

        Tile t = new Tile();
        t.type = tiles.get(id);

        return t;
    }

    public static Actor getActor(String id) {
        if (actors == null) {
            actors = new HashMap<String, JsonValue>();

            for (JsonValue actor : new JsonReader().parse(Gdx.files.internal("data/actors.json")).get("actors")) {
                actors.put(actor.getString("id"), actor);
            }
        }

        JsonValue json = actors.get(id);
        Actor b = null;

        String inherit = json.getString("inherit");

        if (inherit.equals("Humanoid")) {
            b = new Humanoid();
        } else if (inherit.equals("Slime")) {
            b = new Slime();
        }

        if (b != null) {
            b.deserialize(json);
        }

        return b;
    }

    public static void dispose() {
        for (Texture t : textures) {
            t.dispose();
        }
    }
}
