package game.main.gen;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import game.loader.*;
import game.main.play.Play;
import game.main.play.entity.Entity;
import game.main.play.entity.Humanoid;
import game.main.play.structure.Structure;
import game.main.play.tile.Tile;
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

    public Array<Room> rooms;

    public Biome[][] biomes;

    public Array<Array<Room>> rivers;

    public WorldGenerator(Play play) {
        this.play = play;
    }

    public void generate() {
        rand = new Random(MathUtils.random(Long.MAX_VALUE));

        rooms = new Array<Room>();

        rooms.add(new Room(0, 0, 1, 1));

        while (true) {
            int size = rooms.size;

            for (int i = 0; i < size; i++) {
                Room r = rooms.get(i);

                Room room = new Room();

                room.w = 1;
                room.h = 1;

                if (rand.nextFloat() < .5f) {
                    if (rand.nextFloat() < .5f) {
                        room.w = 2;
                    } else {
                        room.h = 2;
                    }

                    if (rand.nextFloat() < .5f) {
                        room.w = 2;
                        room.h = 2;
                    }
                }

                switch (rand.nextInt(4)) {
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

                if (room.x >= 0 && room.y >= 0 && room.x + room.w <= WIDTH * 2 && room.y + room.h <= HEIGHT * 2) {
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

        noise = new Noise(rand);

        biomes = new Biome[WIDTH][HEIGHT];

        for (int x = 0; x < biomes.length; x++) {
            for (int y = 0; y < biomes[0].length; y++) {
                biomes[x][y] = biomeAt(x, y);
            }
        }

        /*
        rivers = new Array<Array<Room>>();

        int lastX = -1;

        for (int i = 0; i < 1 + rand.nextInt(3); i++) {
            int x;
            int y;

            do {
                x = rand.nextInt(biomes.length);
                y = rand.nextInt(biomes[0].length);
            } while (biomes[x][y] != mountains || (roomAt(x * 2, y) != null && roomAt(x * 2, y).w == 1) || x == lastX);

            lastX = x;

            Array<Room> river = new Array<Room>();
            river.add(roomAt(x * 2, y));

            boolean firstStep = false;
            boolean goneHorizontal = false;

            do {
                if (!firstStep) {
                    y--;
                    firstStep = true;
                } else {
                    if (rand.nextFloat() < .75f || goneHorizontal) {
                        y--;
                        goneHorizontal = false;
                    } else {
                        goneHorizontal = true;

                        if (rand.nextFloat() < .5f) {
                            if (x < biomes.length - 1) {
                                x++;
                            }
                        } else {
                            if (x > 0) {
                                x--;
                            }
                        }
                    }
                }

                river.add(roomAt(x * 2, y));
            } while (biomes[x][y] != ocean);

            // Remove the last because it's already the ocean
            river.removeIndex(river.size - 1);

            roomAt(river.first().x, river.first().y).riverSource = true;

            rivers.add(river);
        }
        */

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
                for (float p = 0; p < 1; p += .01f) {
                    int x = (int) (MathUtils.lerp(r0.x + r0.w * .5f, r1.x + r1.w * .5f, p) * 8f);
                    int y = (int) (MathUtils.lerp(r0.y + r0.h * .5f, r1.y + r1.h * .5f, p) * 4f);

                    for (int xx = -1; xx <= 0; xx++) {
                        for (int yy = -1; yy <= 0; yy++) {
                            play.placeTile(TileLoader.load("Grass"), x + xx, y + yy, 0);

                            for (int z = 1; z < play.tiles[0][0].length; z++) {
                                play.tiles[x + xx][y + yy][z] = null;
                            }
                        }
                    }
                }
            }
        }

        /*
        for (Array<Room> river : rivers) {
            Room source = river.first();

            {
                int x0 = source.x * 8;
                int x1 = x0 + source.w * 8;

                int y0 = source.y * 9;
                int y1 = y0 + source.h * 9;

                final int indent = 2;

                for (int x = x0; x < x1; x++) {
                    for (int y = y0; y < y1; y++) {
                        if (x >= x0 + indent && x < x1 - indent && y >= y0 + indent && y < y1 - indent) {
                            play.placeTile(TileLoader.load("Water"), x, y, 0);
                        }
                    }
                }

                for (float p = 0; p <= 1; p += .1f) {
                    int x = x0 + (int) MathUtils.lerp(0, indent, p);
                    int y = y0 + (int) MathUtils.lerp(0, indent, p);

                    for (int xx = 0; xx < 2; xx++) {
                        for (int yy = 0; yy < 2; yy++) {
                            play.placeTile(TileLoader.load("Water"), x + xx, y + yy, 0);
                        }
                    }
                }
            }

            for (int i = 0; i < river.size - 1; i++) {
                Room r0 = river.get(i);
                Room r1 = river.get(i + 1);

                for (float p = 0; p < 1; p += .01f) {
                    int x = (int) (MathUtils.lerp(r0.x, r1.x, p) * 8f);
                    int y = (int) (MathUtils.lerp(r0.y, r1.y, p) * 9f);

                    for (int xx = -1; xx <= 0; xx++) {
                        for (int yy = -1; yy <= 1; yy++) {
                            play.placeTile(TileLoader.load("Water"), x + xx, y + yy, 0);

                            for (int z = 1; z < play.tiles[0][0].length; z++) {
                                try {
                                    play.tiles[x + xx][y + yy][z] = null;
                                } catch (IndexOutOfBoundsException e) {
                                }
                            }
                        }
                    }
                }
            }
        }

        for (Room r0 : rooms) {
            if (!r0.riverSource) {
                for (Room r1 : r0.children) {
                    if (!r1.riverSource) {
                        for (float p = 0; p < 1; p += .01f) {
                            int x = (int) (MathUtils.lerp(r0.x + r0.w * .5f, r1.x + r1.w * .5f, p) * 8f);
                            int y = (int) (MathUtils.lerp(r0.y + r0.h * .5f, r1.y + r1.h * .5f, p) * 9f);

                            for (int xx = -1; xx <= 0; xx++) {
                                for (int yy = -1; yy <= 1; yy++) {
                                    if (play.tileAt(x + xx, y + yy, 0).type.name.equals("Water")) {
                                        play.placeTile(TileLoader.load("Stone"), x + xx, y + yy, 0);

                                        for (int z = 1; z < play.tiles[0][0].length; z++) {
                                            play.tiles[x + xx][y + yy][z] = null;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        */

        play.solidTiles = new Rectangle[play.tiles.length][play.tiles[0].length];

        play.entities = new Array<Entity>();

        Humanoid h = new Humanoid();
        h.feet = SheetLoader.load("HumanFeet");
        h.body = SheetLoader.load("HumanBody");
        h.head = SheetLoader.load("HumanHead");
        h.color = ColorLoader.load("Peach");
        h.animSpeed = 7.5f;

        h.x = 64;
        h.y = 36;

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
}
