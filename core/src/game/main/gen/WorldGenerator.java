package game.main.gen;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import game.main.Game;
import game.main.item.equipment.armor.BodyArmor;
import game.main.item.equipment.armor.FeetArmor;
import game.main.item.equipment.hand.main.MainHand;
import game.main.item.equipment.hand.off.OffHand;
import game.main.state.play.Play;
import game.main.state.play.entity.Slime;
import game.main.state.play.entity.Humanoid;
import game.main.state.play.tile.Connector;
import game.main.state.play.tile.Tile;
import game.noise.Noise;
import game.noise.NoiseParameters;

import java.awt.*;
import java.util.HashMap;
import java.util.Objects;
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

    public Array<River> rivers;

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

        rooms = new Array<>();

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

                // Make sure the rooms don'map overlap multiple screens
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

        // Reduce linearity by connecting adjacent rooms
        rooms.forEach(r0 -> {
            Room[] rooms = new Room[] {
                    roomAt(r0.x + r0.w, r0.y),
                    roomAt(r0.x - 1, r0.y),
                    roomAt(r0.x, r0.y + r0.h),
                    roomAt(r0.x, r0.y - 1)
            };

            for (int i = 0; i < rooms.length; i++) {
                Room r1 = rooms[i];

                if (r1 != null && r0.parent != r1 && !r0.children.contains(r1, true) && rand.nextFloat() < biomes[r0.x / 2][r0.y / 2].randomConnectChance) {
                    r0.children.add(r1);
                }
            }
        });

        rivers = new Array<>();

        for (int i = 0; i < 2 + rand.nextInt(3); i++) {
            int x;
            int y;

            do {
                x = rand.nextInt(biomes.length) * 2;
                y = rand.nextInt(biomes[0].length) * 2;
            } while (biomes[x / 2][y / 2] != Game.BIOMES.load("Mountains") || (roomAt(x, y) != null && (roomAt(x, y).w == 1 || roomAt(x, y).h == 1)));

            River river = new River(1);
            river.points.add(new Point(x, y));

            boolean goneHorizontal = false;

            do {
                if (rand.nextFloat() < (biomes[x / 2][y / 2] == Game.BIOMES.load("Mountains") ? 0 : .5f) && !goneHorizontal) {
                    goneHorizontal = true;

                    if (rand.nextFloat() < .5f) {
                        if (x > 1) {
                            x -= 2;
                        }
                    } else {
                        if (x < biomes.length * 2 - 2) {
                            x += 2;
                        }
                    }
                } else {
                    goneHorizontal = false;
                    y -= 2;
                }

                Point p = new Point(x, y);

                if (!river.points.contains(p, true)) {
                    river.points.add(p);
                }
            } while (y > 0);

            // Remove last because it's the ocean
            river.points.removeIndex(river.points.size - 1);

            rivers.add(river);
        }

        play.tiles = new Tile[WIDTH * 16][HEIGHT * 8][8];

        for (Room r : rooms) {
            int x0 = r.x0();
            int x1 = r.x1();

            int y0 = r.y0();
            int y1 = r.y1();

            Biome b = biomes[r.x / 2][r.y / 2];

            for (int x = x0; x < x1; x++) {
                for (int y = y0; y < y1; y++) {
                    play.placeTile(b.ground, x, y, 0);

                    if (b.wall != null) {
                        if ((x == x0 || x == x1 - 1 || y == y0 || y == y1 - 1)) {
                            b.wall.generate(rand, play, x, y, 0);
                        }
                    }
                }
            }
        }

        for (Room r : rooms) {
            int x0 = r.x0();
            int y1 = r.y1();

            Biome b = biomes[r.x / 2][r.y / 2];
            RoomTemplate t = b.pickTemplate(r, rand);

            if (t != null) {
                char[][] layout = t.copyLayout();

                // Flip vertically
                if (rand.nextFloat() < t.verticalFlipChance) {
                    for (int y = 0; y < layout.length / 2; y++) {
                        char[] old = layout[y];

                        layout[y] = layout[layout.length - 1 - y];
                        layout[layout.length - 1 - y] = old;
                    }
                }

                // Flip horizontally
                if (rand.nextFloat() < t.horizontalFlipChance) {
                    for (int y = 0; y < layout.length; y++) {
                        for (int x = 0; x < layout[0].length / 2; x++) {
                            char old = layout[y][x];

                            layout[y][x] = layout[y][layout[0].length - 1 - x];
                            layout[y][layout[0].length - 1 - x] = old;
                        }
                    }
                }

                for (int y = 0; y < layout.length; y++) {
                    for (int x = 0; x < layout[0].length; x++) {
                        if (layout[y][x] == '#') {
                            b.wall.generate(rand, play, x0 + x, y1 - 1 - y, 0);
                        }
                    }
                }
            }
        }

        HashMap<Room, Array<Room>> connected = new HashMap<>();

        for (Room r0 : rooms) {
            for (Room r1 : r0.children) {
                if (!connected.containsKey(r1) || !connected.get(r1).contains(r0, true)) {
                    Connection connection = getConnection(r0, r1);

                    connection.points.forEach(p -> {
                        Tile t = play.tileAt(p.x, p.y, 0);

                        Biome b = biomes[p.x / 16][p.y / 8];
                        Connector c = b.getConnector(connection);

                        if (t != null && b.wall != null && t.type == b.wall.getTile()) {
                            play.erase(p.x, p.y);
                            play.placeTile(c.tile, p.x, p.y, 0);
                        }
                    });

                    if (!connected.containsKey(r0)) {
                        connected.put(r0, new Array<>());
                    }

                    connected.get(r0).add(r1);
                }
            }
        }

        for (River river : rivers) {
            for (int i = 0; i < river.points.size - 1; i++) {
                Point p0 = river.points.get(i);
                Point p1 = river.points.get(i + 1);

                Biome b = biomes[p1.x / 2][p1.y / 2];

                for (float p = 0; p < 1; p += .01f) {
                    int x = (int) MathUtils.lerp(p0.x * 8, p1.x * 8, p);
                    int y = (int) MathUtils.lerp(p0.y * 4, p1.y * 4, p);

                    for (int xx = -river.thickness; xx <= river.thickness - 1; xx++) {
                        for (int yy = -1; yy <= 0; yy++) {
                            Tile t = play.tileAt(x + xx, y + yy, 0);

                            if (t != null && t.type != b.river) {
                                play.erase(x + xx, y + yy);
                                play.placeTile(b.river, x + xx, y + yy, 0);
                            }
                        }
                    }
                }
            }
        }

        for (Room r0 : rooms) {
            for (Room r1 : r0.children) {
                Biome b = biomes[r1.x / 2][r1.y / 2];

                getConnection(r0, r1).points.forEach(p -> {
                    Tile t = play.tileAt(p.x, p.y, 0);

                    if (t != null && t.type == b.river) {
                        play.placeTile(b.riverBridge, p.x, p.y, 0);
                    }
                });
            }
        }

        play.solidTiles = new Rectangle[play.tiles.length][play.tiles[0].length];

        play.entities = new Array<>();
        play.invisibleEntities = new Array<>();

        for (int x = 0; x < play.tiles.length; x++) {
            for (int y = 0; y < play.tiles[0].length; y++) {
                if (play.isFree(x, y, 0, 1) && rand.nextFloat() < .1f) {
                    Slime s = (Slime) Game.ENTITIES.load("GreenSlime");
                    s.x = x * 8;
                    s.y = y * 8;
                    play.add(s);
                }
            }
        }

        Humanoid h = (Humanoid) Game.ENTITIES.load("Human");

        h.mainHand = (MainHand) Game.ITEMS.load("Axe");
        h.offHand = (OffHand) Game.ITEMS.load("Shield");
        h.feetArmor = (FeetArmor) Game.ITEMS.load("FeetArmor");
        h.bodyArmor = (BodyArmor) Game.ITEMS.load("BodyArmor");

        h.x = biomes.length * 64 + 64;
        h.y = (biomes[0].length - 1) * 64 - 32;

        play.player = h;
        play.add(h);

        play.positionCam();
    }

    public Room roomAt(int x, int y) {
        for (int i = 0; i < rooms.size; i++) {
            Room r = rooms.get(i);

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

        for (Biome b : Game.BIOMES.loadAll()) {
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

    public Connection getConnection(Room r0, Room r1) {
        Connection c = new Connection();

        Room smaller = smaller(r0, r1);
        Room larger = larger(r0, r1);

        if (smaller == null && larger == null) {
            smaller = r0;
            larger = r1;
        }

        int diffX = larger.x - Objects.requireNonNull(smaller).x;

        c.absoluteDiffX = Math.abs(diffX);

        if (diffX != 0) {
            diffX /= c.absoluteDiffX;
        }

        int diffY = larger.y - smaller.y;

        c.absoluteDiffY = Math.abs(diffY);

        if (diffY != 0) {
            diffY /= c.absoluteDiffY;
        }

        Biome b = biomes[r0.x / 2][r0.y / 2];
        c.wayThickness = b.wayThickness[rand.nextInt(b.wayThickness.length)];

        if (diffX != 0) {
            int[] xx = new int[] {smaller.x * 8 + smaller.w * 4, smaller.x * 8 + smaller.w * 4 + larger.w * diffX * 8};

            int x0 = Math.min(xx[0], xx[1]);
            int x1 = Math.max(xx[0], xx[1]);

            for (int x = x0; x < x1; x++) {
                int y0 = smaller.y * 4 + smaller.h * 2 - 1;
                int y1 = y0 + c.wayThickness;

                for (int y = y0; y < y1; y++) {
                    c.points.add(new Point(x, y));
                }
            }
        } else if (diffY != 0) {
            int[] yy = new int[] {smaller.y * 4 + smaller.h * 2, smaller.y * 4 + smaller.h * 2 + larger.h * diffY * 4};

            int y0 = Math.min(yy[0], yy[1]);
            int y1 = Math.max(yy[0], yy[1]);

            for (int y = y0; y < y1; y++) {
                int x0 = smaller.x * 8 + smaller.w * 4 - 1;
                int x1 = x0 + c.wayThickness;

                for (int x = x0; x < x1; x++) {
                    c.points.add(new Point(x, y));
                }
            }
        }

        return c;
    }

    public class River {
        public Array<Point> points;
        public int thickness;

        public River(int thickness) {
            points = new Array<>();
            this.thickness = thickness;
        }
    }

    public class Connection {
        public Array<Point> points;

        public int absoluteDiffX;
        public int absoluteDiffY;

        public int wayThickness;

        public Connection() {
            points = new Array<>();
        }
    }
}
