package game.main.state.play.map.dungeon;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.map.Map;
import game.main.state.play.map.entity.Humanoid;
import game.main.state.play.map.tile.Tile;

public class Dungeon extends Map {
    public static class Room {
        public enum Type {
            HORIZONTAL,
            VERTICAL
        }

        public Rectangle rect;

        public Room parent;
        public Array<Room> children;

        public Room(int x, int y, int w, int h) {
            this.rect = new Rectangle(x, y, w, h);
            children = new Array<Room>();
        }

        public Room() {
            this(0, 0, 0, 0);
        }

        public int x0() {
            return (int) (rect.x * 16);
        }

        public int x1() {
            return (int) (rect.x * 16 + rect.width * 16);
        }

        public int getCenterX() {
            return x0() + (x1() - x0()) / 2;
        }

        public int y0() {
            return (int) (rect.y * 8);
        }

        public int y1() {
            return (int) (rect.y * 8 + rect.height * 8);
        }

        public int getCenterY() {
            return y0() + (y1() - y0()) / 2;
        }

        public boolean isOpenRight() {
            for (Room child : children) {
                if (child.x0() > x0()) {
                    return true;
                }
            }

            return false;
        }

        public boolean isOpenUp() {
            for (Room child : children) {
                if (child.y0() > y0()) {
                    return true;
                }
            }

            return false;
        }

        public boolean isOpenLeft() {
            for (Room child : children) {
                if (child.x0() < x0()) {
                    return true;
                }
            }

            return false;
        }

        public boolean isOpenDown() {
            for (Room child : children) {
                if (child.y0() < y0()) {
                    return true;
                }
            }

            return false;
        }

        public Type getType() {
            if (isOpenRight() && isOpenLeft() && !isOpenUp() && !isOpenDown()) {
                return Type.HORIZONTAL;
            }

            if (isOpenUp() && isOpenDown() && !isOpenRight() && !isOpenLeft()) {
                return Type.VERTICAL;
            }

            return null;
        }
    }

    public static class Template implements Serializable {
        public Room.Type roomType;
        public char[][] template;

        public Template(JsonValue json) {
            deserialize(json);
        }

        @Override
        public void deserialize(JsonValue json) {
            JsonValue roomType = json.get("roomType");
            if (roomType != null) {
                this.roomType = Room.Type.valueOf(roomType.asString());
            }

            JsonValue template = json.get("template");
            if (template != null) {
                this.template = new char[16][8];

                int y = 0;

                for (JsonValue row : template) {
                    int x = 0;

                    for (JsonValue col : row) {
                        this.template[x][this.template[0].length - 1 - y] = col.asChar();

                        x++;
                    }

                    y++;
                }
            }
        }
    }

    public static final int WIDTH = 16;
    public static final int HEIGHT = 8;

    public static final int ROOMS = 10;

    public Array<Room> rooms;
    public Template[] templates;

    @Override
    public void generate() {
        super.generate();

        // Generate room layout
        rooms = new Array<Room>();

        rooms.add(new Room(Game.RANDOM.nextInt(WIDTH), Game.RANDOM.nextInt(HEIGHT), 1, 1));

        while (true) {
            int size = rooms.size;
            boolean done = false;

            for (int i = 0; i < size; i++) {
                if (rooms.size >= ROOMS) {
                    done = true;
                    break;
                }

                Room r = rooms.get(i);

                Room room = new Room();

                room.rect.width = 1;
                room.rect.height = 1;

                int dir;

                if (Game.RANDOM.nextFloat() < .5f) {
                    dir = Game.RANDOM.nextFloat() < .5f ? 0 : 2;
                } else {
                    dir = Game.RANDOM.nextFloat() < .5f ? 1 : 3;
                }

                switch (dir) {
                    case 0:
                        room.rect.x = r.rect.x + r.rect.width;
                        room.rect.y = r.rect.y;
                        break;
                    case 2:
                        room.rect.x = r.rect.x - room.rect.width;
                        room.rect.y = r.rect.y;
                        break;
                    case 1:
                        room.rect.x = r.rect.x;
                        room.rect.y = r.rect.y + r.rect.height;
                        break;
                    case 3:
                        room.rect.x = r.rect.x;
                        room.rect.y = r.rect.y - room.rect.height;
                        break;
                }

                if (room.rect.x >= 0 && room.rect.x + room.rect.width <= WIDTH
                        && room.rect.y >= 0 && room.rect.y + room.rect.height <= HEIGHT) {
                    boolean overlap = false;

                    for (int j = 0; j < this.rooms.size; j++) {
                        Room otherRoom = this.rooms.get(j);

                        if (room.rect.overlaps(otherRoom.rect)) {
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

            if (done) {
                break;
            }
        }

        tiles.initSize(WIDTH * 16, HEIGHT * 8, 8);

        // Apply templates
        for (Room r : rooms) {
            Room.Type roomType = r.getType();
            Template t = null;

            for (Template template : templates) {
                if (roomType == template.roomType) {
                    t = template;
                }
            }

            if (t != null) {
                for (int x = 0; x < t.template.length; x++) {
                    for (int y = 0; y < t.template[0].length; y++) {
                        int xx = r.x0() + x;
                        int yy = r.y0() + y;

                        switch (t.template[x][y]) {
                            case '#':
                                tiles.set(Game.TILES.load("DungeonWall"), xx, yy, 0);
                                break;
                            case ' ':
                                tiles.set(Game.TILES.load("DungeonGround"), xx, yy, 0);
                                break;
                        }
                    }
                }
            }
        }

        // Connect parent rooms with its children
        for (int i = 0; i < rooms.size; i++) {
            Room r0 = rooms.get(i);

            for (int j = 0; j < r0.children.size; j++) {
                Room r1 = r0.children.get(j);

                for (float p = 0; p < 1; p += .01f) {
                    int x = (int) MathUtils.lerp(r0.getCenterX(), r1.getCenterX(), p);
                    int y = (int) MathUtils.lerp(r0.getCenterY(), r1.getCenterY(), p);

                    for (int xx = -1; xx <= 0; xx++) {
                        for (int yy = -1; yy <= 0; yy++) {
                            int xxx = x + xx;
                            int yyy = y + yy;

                            Tile t = tiles.at(xxx, yyy, 0);

                            if (t != null && t.name.equals("DungeonWall")) {
                                tiles.set(Game.TILES.load("DungeonGround"), xxx, yyy, 0);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void placePlayer() {
        Room r = rooms.first();

        Humanoid player = (Humanoid) Game.ENTITIES.load("Human");
        player.controlledByPlayer = true;
        player.x = (r.rect.x + r.rect.width * .5f) * 16f * 8f;
        player.y = (r.rect.y + r.rect.height * .5f) * 8f * 8f;

        this.player = player;
        entities.addEntity(player);
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue templates = json.get("templates");
        if (templates != null) {
            this.templates = new Template[templates.size];

            int i = 0;
            for (JsonValue template : templates) {
                this.templates[i] = new Template(template);
                i++;
            }
        }
    }
}
