package game.main.gen;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import game.loader.StructureLoader;
import game.loader.TileLoader;
import game.main.play.Play;
import game.main.play.entity.Entity;
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

        rooms.add(new Room(0, 0, 2, 1));

        while (true) {
            int size = rooms.size;

            for (int i = 0; i < size; i++) {
                Room r = rooms.get(i);

                Room room = new Room();
                room.w = 2;
                room.h = 1;

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

                if (room.x >= 0 && room.y >= 0 && room.x + room.w <= WIDTH * 2 && room.y + room.h <= HEIGHT) {
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
                for (float y = 0; y < HEIGHT; y++) {
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

        for (int i = rooms.size - 1; i >= 0; i--) {
            Room r = rooms.get(i);

            if (r.parent != null && rand.nextFloat() < .2f) {
                Room r1 = new Room(r.x, r.y, 1, r.h);
                Room r2 = new Room(r.x + 1, r.y, 1, r.h);

                r.parent.children.removeValue(r, true);

                r.parent.children.add(r1);
                r1.parent = r.parent;

                r2.parent = r1;
                r1.children.add(r2);

                for (Room child : r.children) {
                    child.parent = r2;
                    r2.children.add(child);
                }

                rooms.addAll(r1, r2);
                rooms.removeValue(r, true);
            }
        }

        biomes = new Biome[WIDTH][HEIGHT];

        noise = new Noise(rand);

        Biome mountains = new Biome();
        mountains.ground = TileLoader.load("Stone");

        Biome forest = new Biome();
        forest.ground = TileLoader.load("Grass");
        forest.wall = StructureLoader.load("OakTree");

        Biome ocean = new Biome();
        ocean.ground = TileLoader.load("Water");

        for (int x = 0; x < biomes.length; x++) {
            for (int y = 0; y < biomes[0].length; y++) {
                float elev = elevationAt(x, y);

                if (elev > .45f) {
                    biomes[x][y] = mountains;
                } else if (elev > .1f) {
                    biomes[x][y] = forest;
                } else {
                    biomes[x][y] = ocean;
                }
            }
        }

        rivers = new Array<Array<Room>>();

        for (int i = 0; i < 1; i++) {
            int x;
            int y;

            do {
                x = rand.nextInt(biomes.length);
                y = rand.nextInt(biomes[0].length);
            } while (biomes[x][y] != mountains || (roomAt(x * 2, y) != null && roomAt(x * 2, y).w == 1));

            Array<Room> river = new Array<Room>();
            river.add(roomAt(x * 2, y));

            do {
                if (rand.nextFloat() < .75f) {
                    y--;
                } else {
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

                river.add(roomAt(x * 2, y));
            } while (biomes[x][y] != ocean);

            rivers.add(river);
        }

        for (Array<Room> river : rivers) {
            for (Room room : river) {
                biomes[room.x / 2][room.y] = ocean;
            }
        }

        play.tiles = new Tile[WIDTH * 16][HEIGHT * 9][8];

        for (Room r : rooms) {
            int x0 = r.x * 8;
            int x1 = x0 + r.w * 8;

            int y0 = r.y * 9;
            int y1 = y0 + r.h * 9;

            for (int x = x0; x < x1; x++) {
                for (int y = y0; y < y1; y++) {
                    Biome b = biomes[r.x / 2][r.y];

                    play.placeTile(b.ground, x, y, 0);

                    if (b.wall != null) {
                        if (x == x0 || x == x1 - 1 || y == y0 || y == y1 - 1) {
                            b.wall.generate(rand, play, x, y, 0);
                        }
                    }
                }
            }
        }

        for (Room r : rooms) {
            for (Room child : r.children) {
                if (child.y == r.y) {
                    int x0 = (int) ((r.x + r.w * .5f) * 8f);
                    int x1 = (int) ((child.x + child.w * .5f) * 8f);

                    int y = (int) ((r.y + r.h * .5f + child.y + child.h * .5f) * .5f * 9f);

                    for (int x = Math.min(x0, x1); x < Math.max(x0, x1); x++) {
                        for (int yy = -1; yy <= 1; yy++) {
                            if (play.tiles[x][y + yy][0] != null) {
                                play.placeTile(TileLoader.load("Grass"), x, y + yy, 0);

                                for (int z = 1; z < play.tiles[0][0].length; z++) {
                                    play.tiles[x][y + yy][z] = null;
                                }
                            }
                        }
                    }
                } else {
                    int y0 = (int) ((r.y + r.h * .5f) * 9f);
                    int y1 = (int) ((child.y + child.h * .5f) * 9f);

                    int x = (int) ((r.x + r.w * .5f + child.x + child.w * .5f) * .5f * 8f);

                    if (child.w == 1) {
                        x = (int) ((child.x + child.w * .5f) * 8f);
                    }

                    if (r.w == 1) {
                        x = (int) ((r.x + r.w * .5f) * 8f);
                    }

                    for (int y = Math.min(y0, y1); y < Math.max(y0, y1); y++) {
                        for (int xx = -1; xx <= 0; xx++) {
                            if (play.tiles[x + xx][y][0] != null) {
                                play.placeTile(TileLoader.load("Grass"), x + xx, y, 0);

                                for (int z = 1; z < play.tiles[0][0].length; z++) {
                                    play.tiles[x + xx][y][z] = null;
                                }
                            }
                        }
                    }
                }
            }
        }

        play.solidTiles = new Rectangle[play.tiles.length][play.tiles[0].length];

        play.entities = new Array<Entity>();
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
}
