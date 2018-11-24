package mystiqa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.entity.being.Being;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.entity.being.humanoid.race.HumanoidRace;
import mystiqa.entity.being.slime.Slime;
import mystiqa.entity.tile.Tile;
import mystiqa.entity.tile.TileType;
import mystiqa.world.Biome;

import java.util.HashMap;

public class Assets {
    public static final String HUMANOID_RACES = "data/humanoid_races/";
    public static final String BEINGS = "data/entities/beings/";

    private static Assets instance;

    private Array<Texture> textures;
    private HashMap<String, TextureRegion[][]> spriteSheets;

    private HashMap<String, Color> colors;

    private HashMap<String, Biome> biomes;

    private HashMap<String, HumanoidRace> humanoidRaces;

    private HashMap<String, TileType> tiles;

    private HashMap<String, JsonValue> beings;

    private Assets() {

    }

    public Array<FileHandle> getFiles(FileHandle root) {
        Array<FileHandle> files = new Array<FileHandle>();
        getFiles(root, files);
        return files;
    }

    public void getFiles(FileHandle root, Array<FileHandle> files) {
        for (FileHandle file : root.list()) {
            if (file.isDirectory()) {
                getFiles(file, files);
            } else {
                files.add(file);
            }
        }
    }

    public TextureRegion[][] getSpriteSheet(String id) {
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

    public Color getColor(String id) {
        if (colors == null) {
            colors = new HashMap<String, Color>();

            for (JsonValue color : new JsonReader().parse(Gdx.files.internal("data/colors.json")).get("colors")) {
                colors.put(color.getString("id"), new Color(color.getInt("r") / 255f, color.getInt("g") / 255f, color.getInt("b") / 255f, 1));
            }
        }

        return colors.get(id);
    }

    public Biome getBiome(String id) {
        if (biomes == null) {
            biomes = new HashMap<String, Biome>();

            for (JsonValue biome : new JsonReader().parse(Gdx.files.internal("data/biomes.json")).get("biomes")) {
                Biome b = new Biome();
                b.deserialize(biome);

                this.biomes.put(biome.getString("id"), b);
            }
        }

        return biomes.get(id);
    }

    public HumanoidRace getHumanoidRace(String name) {
        if (humanoidRaces == null) {
            humanoidRaces = new HashMap<String, HumanoidRace>();
        }

        if (!humanoidRaces.containsKey(name)) {
            for (FileHandle f : getFiles(Gdx.files.internal(HUMANOID_RACES))) {
                if (f.nameWithoutExtension().equals(name)) {
                    HumanoidRace race = new HumanoidRace();
                    race.deserialize(new JsonReader().parse(f));

                    humanoidRaces.put(name, race);

                    break;
                }
            }
        }

        return humanoidRaces.get(name);
    }

    public Tile getTile(String id) {
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

    public Being getBeing(String name) {
        if (beings == null) {
            beings = new HashMap<String, JsonValue>();
        }

        if (!beings.containsKey(name)) {
            for (FileHandle f : getFiles(Gdx.files.internal(BEINGS))) {
                if (f.nameWithoutExtension().equals(name)) {
                    beings.put(name, new JsonReader().parse(f));
                    break;
                }
            }
        }

        JsonValue json = beings.get(name);
        Being b = null;

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

    public void dispose() {
        for (Texture t : textures) {
            t.dispose();
        }
    }

    public static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }

        return instance;
    }
}
