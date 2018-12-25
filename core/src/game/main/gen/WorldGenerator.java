package game.main.gen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import game.loader.*;
import game.loader.palette.PaletteShader;
import game.loader.palette.PaletteShaderLoader;
import game.main.item.equipment.armor.BodyArmor;
import game.main.item.equipment.armor.FeetArmor;
import game.main.item.equipment.hand.main.MeleeWeapon;
import game.main.item.equipment.hand.off.Shield;
import game.main.play.Play;
import game.main.play.entity.Entity;
import game.main.play.entity.humanoid.Humanoid;
import game.main.play.tile.Tile;
import game.main.stat.StatType;
import game.noise.Noise;
import game.noise.NoiseParameters;

import java.util.Random;

public class WorldGenerator {
    public static final int WIDTH = 16;
    public static final int HEIGHT = 8;

    public static final NoiseParameters ELEVATION = new NoiseParameters(3, .25f, 1);

    public Play play;

    public Random rand;

    public Noise noise;

    public Biome[][] biomes;

    public Array<Room> rooms;

    public Array<Array<Room>> rivers;

    public WorldGenerator(Play play) {
        this.play = play;
    }

    public void generate() {
        rand = new Random(MathUtils.random(Long.MAX_VALUE));

        noise = new Noise(rand);

        biomes = new Biome[WIDTH][HEIGHT];

        for (int x = 0; x < biomes.length; x++) {
            for (int y = 0; y < biomes[0].length; y++) {
                biomes[x][y] = biomeAt(x, y);
            }
        }

        rooms = new Array<Room>();

        // This is where it starts
        rooms.add(new Room(0, 0, 2, 2));

        while (true) {
            int size = rooms.size;

            for (int i = 0; i < size; i++) {
                Room r = rooms.get(i);
                Biome b = biomeAt(r.x / 2, r.y / 2);

                Room room = new Room();

                room.w = 1;
                room.h = 1;

                for (RoomSize roomSize : b.roomSizes) {
                    if (rand.nextFloat() < roomSize.chance) {
                        room.w = roomSize.width;
                        room.h = roomSize.height;
                    }
                }

                int dir;

                if (rand.nextFloat() < biomeAt(r.x / 2, r.y / 2).horizontalChance) {
                    dir = rand.nextFloat() < .5f ? 0 : 2;
                } else {
                    dir = rand.nextFloat() < .5f ? 1 : 3;
                }

                switch (dir) {
                    case 0:
                        room.x = r.x + r.w;
                        room.y = r.y;
                        break;
                    case 2:
                        room.x = r.x - room.w;
                        room.y = r.y;
                        break;
                    case 1:
                        room.x = r.x;
                        room.y = r.y + r.h;
                        break;
                    case 3:
                        room.x = r.x;
                        room.y = r.y - room.h;
                        break;
                }

                // Make sure the rooms don't overlap multiple screens
                boolean position = true;

                if (room.w > 1 && room.x % 2 != 0) {
                    position = false;
                }

                if (room.h > 1 && room.y % 2 != 0) {
                    position = false;
                }

                if (position && room.x >= 0 && room.y >= 0 && room.x + room.w <= WIDTH * 2 && room.y + room.h <= HEIGHT * 2) {
                    boolean overlap = false;

                    for (int j = 0; j < this.rooms.size; j++) {
                        Room _r = this.rooms.get(j);

                        if (room.x < _r.x + _r.w && room.x + room.w > _r.x && room.y < _r.y + _r.h && room.y + room.h > _r.y) {
                            overlap = true;
                            break;
                        }
                    }

                    if (!overlap) {
                        room.parent = r;
                        r.children.add(room);

                        rooms.add(room);
                    }
                }
            }

            boolean full = true;

            for (float x = 0; x < WIDTH * 2; x++) {
                for (float y = 0; y < HEIGHT * 2; y++) {
                    boolean hasRoom = false;

                    for (Room r : this.rooms) {
                        if (x >= r.x && x < r.x + r.w && y >= r.y && y < r.y + r.h) {
                            hasRoom = true;
                            break;
                        }
                    }

                    if (!hasRoom) {
                        full = false;
                    }
                }
            }

            if (full) {
                break;
            }
        }

        rivers = new Array<Array<Room>>();

        for (int i = 0; i < 1 + rand.nextInt(3); i++) {
            int x;
            int y;

            do {
                x = rand.nextInt(biomes.length * 2);
                y = biomes[0].length * 2 - 1;
            } while (biomes[x / 2][y / 2] != BiomeLoader.load("Mountains") || (roomAt(x, y) != null && (roomAt(x, y).w == 1 || roomAt(x, y).h == 1)));

            Array<Room> river = new Array<Room>();
            river.add(roomAt(x, y));

            boolean goneHorizontal = false;

            do {
                if (rand.nextFloat() < .5f && !goneHorizontal) {
                    goneHorizontal = true;

                    if (rand.nextFloat() < .5f) {
                        if (x > 0) {
                            x--;
                        }
                    } else {
                        if (x < biomes.length * 2 - 1) {
                            x++;
                        }
                    }
                } else {
                    goneHorizontal = false;
                    y--;
                }

                if (!river.contains(roomAt(x, y), true)) {
                    river.add(roomAt(x, y));
                }
            } while (biomes[x / 2][y / 2] != BiomeLoader.load("Ocean"));

            // Remove last because it's the ocean
            river.removeIndex(river.size - 1);

            rivers.add(river);
        }

        play.tiles = new Tile[WIDTH * 16][HEIGHT * 8][8];

        for (Room r : rooms) {
            int x0 = r.x * 8;
            int x1 = x0 + r.w * 8;

            int y0 = r.y * 4;
            int y1 = y0 + r.h * 4;

            for (int x = x0; x < x1; x++) {
                for (int y = y0; y < y1; y++) {
                    Biome b = biomes[r.x / 2][r.y / 2];

                    play.placeTile(b.ground, x, y, 0);

                    if (b.wall != null) {
                        if (x == x0 || x == x1 - 1 || y == y0 || y == y1 - 1) {
                            b.wall.generate(rand, play, x, y, 0);
                        }
                    }
                }
            }
        }

        for (Room r0 : rooms) {
            for (Room r1 : r0.children) {
                Room smaller = smaller(r0, r1);
                Room larger = larger(r0, r1);

                if (smaller == null && larger == null) {
                    smaller = r0;
                    larger = r1;
                }

                float diffX = larger.x - smaller.x;

                if (diffX != 0) {
                    diffX /= Math.abs(diffX);
                }

                float diffY = larger.y - smaller.y;

                if (diffY != 0) {
                    diffY /= Math.abs(diffY);
                }

                if (diffX != 0) {
                    int x0 = (int) ((smaller.x + smaller.w * .5f) * 8f);
                    int x1 = (int) (x0 + (smaller.w * diffX) * 8f);

                    for (int x = Math.min(x0, x1); x < Math.max(x0, x1); x++) {
                        int y0 = (int) ((smaller.y + smaller.h * .5f) * 4f) - 1;
                        int y1 = y0 + 2;

                        for (int y = y0; y < y1; y++) {
                            if (play.tileAt(x, y, 0) != null) {
                                play.placeTile(biomeAt(x / 16, y / 8).ground, x, y, 0);

                                for (int z = 1; z < play.tiles[0][0].length; z++) {
                                    play.tiles[x][y][z] = null;
                                }
                            }
                        }
                    }
                } else if (diffY != 0) {
                    int y0 = (int) ((smaller.y + smaller.h * .5f) * 4f);
                    int y1 = (int) (y0 + (smaller.h * diffY) * 4f);

                    for (int y = Math.min(y0, y1); y < Math.max(y0, y1); y++) {
                        int x0 = (int) ((smaller.x + smaller.w * .5f) * 8f) - 1;
                        int x1 = x0 + 2;

                        for (int x = x0; x < x1; x++) {
                            if (play.tileAt(x, y, 0) != null) {
                                play.placeTile(biomeAt(x / 16, y / 8).ground, x, y, 0);

                                for (int z = 1; z < play.tiles[0][0].length; z++) {
                                    play.tiles[x][y][z] = null;
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Room r : rooms) {
            if (biomeAt(r.x / 2, r.y / 2).minElevation == 0) continue;

            for (int i = 0; i < 10; i++) {
                int x;
                int y;

                do {
                    x = r.x * 8 + rand.nextInt(r.w * 8);
                    y = r.y * 4 + rand.nextInt(r.h * 4);
                } while (play.isFree(x, y, 0, 1));

                if (play.tileAt(x, y, 0).type == biomeAt(r.x / 2, r.y / 2).ground && biomeAt(r.x / 2, r.y / 2).wall != null) {
                    biomeAt(r.x / 2, r.y / 2).wall.generate(rand, play, x, y, 0);
                }
            }
        }

        /*
        for (Array<Room> river : rivers) {
            for (int i = 0; i < river.size - 1; i++) {
                Room r0 = river.get(i);
                Room r1 = river.get(i + 1);

                for (float p = 0; p < 1; p += .01f) {
                    int x = (int) (MathUtils.lerp(r0.x, r1.x, p) * 8f);
                    int y = (int) (MathUtils.lerp(r0.y, r1.y, p) * 4f);

                    for (int xx = -1; xx <= 0; xx++) {
                        for (int yy = -1; yy <= 0; yy++) {
                            if (play.tileAt(x + xx, y + yy, 0) != null) {
                                play.placeTile(TileLoader.load("Water"), x + xx, y + yy, 0);

                                for (int z = 1; z < play.tiles[0][0].length; z++) {
                                    play.tiles[x + xx][y + yy][z] = null;
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Room r0 : rooms) {
            for (Room r1 : r0.children) {
                for (float p = 0; p < 1; p += .01f) {
                    int x = (int) (MathUtils.lerp(r0.x + r0.w * .5f, r1.x + r1.w * .5f, p) * 8f);
                    int y = (int) (MathUtils.lerp(r0.y + r0.h * .5f, r1.y + r1.h * .5f, p) * 4f);

                    for (int xx = -1; xx <= 0; xx++) {
                        for (int yy = -1; yy <= 0; yy++) {
                            if (play.tileAt(x + xx, y + yy, 0) != null && play.tileAt(x + xx, y + yy, 0).type.name.equals("Water")) {
                                play.placeTile(TileLoader.load("Bridge"), x + xx, y + yy, 0);

                                for (int z = 1; z < play.tiles[0][0].length; z++) {
                                    play.tiles[x + xx][y + yy][z] = null;
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Array<Room> river : rivers) {
            Room source = river.first();

            int x0 = source.x * 8;
            int x1 = x0 + source.w * 8;
            int y0 = source.y * 4;
            int y1 = y0 + source.h * 4;

            for (int x = x0; x < x1; x++) {
                for (int y = y0; y < y1; y++) {
                    if (x > x0 + 1 && x < x1 - 2 && y > y0 + 1 && y < y1 - 2) {
                        play.placeTile(TileLoader.load("Water"), x, y, 0);
                    }
                }
            }

            Room next = river.get(1);

            for (float p = 0; p < 1; p += .01f) {
                int x = (int) (MathUtils.lerp(source.x + source.w * .5f, next.x, p) * 8f);
                int y = (int) (MathUtils.lerp(source.y + source.h * .5f, next.y, p) * 4f);

                for (int xx = -1; xx <= 0; xx++) {
                    for (int yy = -1; yy <= 0; yy++) {
                        if (play.tileAt(x + xx, y + yy, 0) != null) {
                            play.placeTile(TileLoader.load("Water"), x + xx, y + yy, 0);

                            for (int z = 1; z < play.tiles[0][0].length; z++) {
                                play.tiles[x + xx][y + yy][z] = null;
                            }
                        }
                    }
                }
            }
        }*/

        play.solidTiles = new Rectangle[play.tiles.length][play.tiles[0].length];

        play.entities = new Array<Entity>();

        Humanoid h = new Humanoid();
        h.type = HumanoidLoader.load("Human");

        MeleeWeapon mw = new MeleeWeapon();
        mw.image = SheetLoader.load("Sword")[0][0];
        mw.palette = PaletteShaderLoader.load(new String[] {"Black", "Gray"});
        mw.angle = 135;
        mw.speed = 1;
        h.mainHand = mw;

        Shield shield = new Shield();
        shield.images = new TextureRegion[] {
                SheetLoader.load("Shield")[0][0],
                SheetLoader.load("Shield")[0][1],
                SheetLoader.load("Shield")[0][2],
                SheetLoader.load("Shield")[0][3]
        };
        shield.palette = PaletteShaderLoader.load(new String[] {"Black", "Gray"});
        h.offHand = shield;

        BodyArmor bodyArmor = new BodyArmor();
        bodyArmor.sheet = SheetLoader.load("BodyArmor");
        bodyArmor.palette = PaletteShaderLoader.load(new String[] {"Black", "Gray"});
        h.bodyArmor = bodyArmor;

        FeetArmor feetArmor = new FeetArmor();
        feetArmor.sheet = SheetLoader.load("FeetArmor");
        feetArmor.palette = PaletteShaderLoader.load(new String[] {"Black", "Gray"});
        h.feetArmor = feetArmor;

        h.stats.addAbsolute(StatType.SPEED, 24);

        h.x = 64;
        h.y = 72 * 4 + 36;

        play.player = h;
        play.entities.add(h);

        play.positionCam();
    }

    public Room roomAt(int x, int y) {
        for (Room r : this.rooms) {
            if (x >= r.x && x < r.x + r.w && y >= r.y && y < r.y + r.h) {
                return r;
            }
        }

        return null;
    }

    public float elevationAt(int x, int y) {
        return MathUtils.clamp((y / (float) biomes[0].length) * noise.noiseAt(x, y, ELEVATION) + (y / (float) biomes[0].length) * .25f, 0, 1);
    }

    public Biome biomeAt(int x, int y) {
        float e = elevationAt(x, y);

        for (Biome b : BiomeLoader.loadAll()) {
            if (e >= b.minElevation && e <= b.maxElevation) {
                return b;
            }
        }

        return null;
    }

    public Room smaller(Room r0, Room r1) {
        int score = 0;

        if (r0.w < r1.w) {
            score++;
        } else if (r0.w > r1.w) {
            score--;
        }

        if (r0.h < r1.h) {
            score++;
        } else if (r0.h > r1.h) {
            score--;
        }

        if (score > 0) {
            return r0;
        } else if (score < 0) {
            return r1;
        }

        return null;
    }

    public Room larger(Room r0, Room r1) {
        int score = 0;

        if (r0.w < r1.w) {
            score--;
        } else if (r0.w > r1.w) {
            score++;
        }

        if (r0.h < r1.h) {
            score--;
        } else if (r0.h > r1.h) {
            score++;
        }

        if (score > 0) {
            return r0;
        } else if (score < 0) {
            return r1;
        }

        return null;
    }
}
