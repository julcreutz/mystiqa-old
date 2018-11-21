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
import mystiqa.world.WorldGenerator;

import java.util.HashMap;

public class Assets {
    public static final String SPRITE_SHEETS = "data/sprite_sheets/";
    public static final String COLORS = "data/colors/";
    public static final String HUMANOID_RACES = "data/humanoid_races/";
    public static final String TILE_TYPES = "data/tile_types/";
    public static final String TILES = "data/entities/tiles/";
    public static final String BEINGS = "data/entities/beings/";
    public static final String BIOMES = "data/world_biomes/";
    public static final String WORLD_GENERATORS = "data/world_generators/";

    private static Assets instance;

    private HashMap<String, Texture> textures;
    private HashMap<Texture, TextureRegion[][]> spriteSheets;

    private HashMap<String, Color> colors;

    private HashMap<String, HumanoidRace> humanoidRaces;

    private HashMap<String, TileType> tileTypes;
    private HashMap<String, JsonValue> tiles;

    private HashMap<String, JsonValue> beings;

    private HashMap<String, Biome> biomes;
    private HashMap<String, WorldGenerator> worldGenerators;

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

    public TextureRegion[][] getSpriteSheet(String name) {
        if (textures == null) {
            textures = new HashMap<String, Texture>();
        }

        if (!textures.containsKey(name)) {
            for (FileHandle f : getFiles(Gdx.files.internal(SPRITE_SHEETS))) {
                if (f.nameWithoutExtension().equals(name)) {
                    JsonValue json = new JsonReader().parse(f);

                    String path = json.getString("path");
                    int splitX = json.getInt("splitX");
                    int splitY = json.getInt("splitY");

                    if (!textures.containsKey(name)) {
                        textures.put(name, new Texture(Gdx.files.internal(path)));
                    }

                    Texture t = textures.get(name);

                    int w = t.getWidth() / splitX;
                    int h = t.getHeight() / splitY;

                    if (spriteSheets == null) {
                        spriteSheets = new HashMap<Texture, TextureRegion[][]>();
                    }

                    TextureRegion[][] spriteSheet = new TextureRegion[w][h];
                    for (int x = 0; x < spriteSheet.length; x++) {
                        for (int y = 0; y < spriteSheet[0].length; y++) {
                            spriteSheet[x][y] = new TextureRegion(t, x * splitX, y * splitY, splitX, splitY);
                        }
                    }

                    spriteSheets.put(t, spriteSheet);

                    break;
                }
            }
        }

        return spriteSheets.get(textures.get(name));
    }

    public Color getColor(String name) {
        if (colors == null) {
            colors = new HashMap<String, Color>();
        }

        if (!colors.containsKey(name)) {
            for (FileHandle f : getFiles(Gdx.files.internal(COLORS))) {
                if (f.nameWithoutExtension().equals(name)) {
                    JsonValue json = new JsonReader().parse(f);

                    Color c = new Color();
                    c.r = json.getInt("r") / 255f;
                    c.g = json.getInt("g") / 255f;
                    c.b = json.getInt("b") / 255f;
                    c.a = 1;

                    colors.put(name, c);

                    break;
                }
            }
        }

        return colors.get(name);
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

    public TileType getTileType(String name) {
        if (tileTypes == null) {
            tileTypes = new HashMap<String, TileType>();
        }

        if (!tileTypes.containsKey(name)) {
            for (FileHandle f : getFiles(Gdx.files.internal(TILE_TYPES))) {
                if (f.nameWithoutExtension().equals(name)) {
                    TileType type = new TileType();
                    type.deserialize(new JsonReader().parse(f));

                    tileTypes.put(name, type);

                    break;
                }
            }
        }

        return tileTypes.get(name);
    }

    public Tile getTile(String name) {
        if (tiles == null) {
            tiles = new HashMap<String, JsonValue>();
        }

        if (!tiles.containsKey(name)) {
            for (FileHandle f : getFiles(Gdx.files.internal(TILES))) {
                if (f.nameWithoutExtension().equals(name)) {
                    tiles.put(name, new JsonReader().parse(f));
                    break;
                }
            }
        }

        Tile t = new Tile();
        t.deserialize(tiles.get(name));

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

    public Biome getBiome(String name) {
        if (biomes == null) {
            biomes = new HashMap<String, Biome>();
        }

        if (!biomes.containsKey(name)) {
            for (FileHandle f : getFiles(Gdx.files.internal(BIOMES))) {
                if (f.nameWithoutExtension().equals(name)) {
                    Biome b = new Biome();
                    b.deserialize((new JsonReader()).parse(f));

                    biomes.put(name, b);

                    break;
                }
            }
        }

        return biomes.get(name);
    }

    public WorldGenerator getWorldGenerator(String name) {
        if (worldGenerators == null) {
            worldGenerators = new HashMap<String, WorldGenerator>();
        }

        if (!worldGenerators.containsKey(name)) {
            for (FileHandle f : getFiles(Gdx.files.internal(WORLD_GENERATORS))) {
                if (f.nameWithoutExtension().equals(name)) {
                    WorldGenerator generator = new WorldGenerator();
                    generator.deserialize(new JsonReader().parse(f));

                    worldGenerators.put(name, generator);

                    break;
                }
            }
        }

        return worldGenerators.get(name);
    }

    public static Assets getInstance() {
        if (instance == null) {
            instance = new Assets();
        }

        return instance;
    }
}
