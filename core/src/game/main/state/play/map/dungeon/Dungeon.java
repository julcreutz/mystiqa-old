package game.main.state.play.map.dungeon;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.item.equipment.armor.Armor;
import game.main.item.equipment.hand.main.MainHand;
import game.main.item.equipment.hand.off.OffHand;
import game.main.state.play.Play;
import game.main.state.play.map.Map;
import game.main.state.play.map.dungeon.lock.PushBlockLock;
import game.main.state.play.map.dungeon.lock.KeyLock;
import game.main.state.play.map.dungeon.lock.Lock;
import game.main.state.play.map.dungeon.lock.KillMonsterLock;
import game.main.state.play.map.entity.Door;
import game.main.state.play.map.entity.Dragon;
import game.main.state.play.map.entity.Entity;
import game.main.state.play.map.entity.Humanoid;
import game.main.state.play.map.tile.Tile;

import java.util.Arrays;

public class Dungeon extends Map {
    public static class Room {
        public Dungeon map;

        public Rectangle rect;

        public Room parent;
        public Array<Room> children;

        public Door doorToParent;

        public Rectangle bounds;
        public Rectangle enterBounds;
        public Array<Entity> monsters;

        public boolean containsPlayer;

        public Room(Dungeon map, int x, int y, int w, int h) {
            this.map = map;

            this.rect = new Rectangle(x, y, w, h);
            children = new Array<Room>();

            bounds = new Rectangle(0, 0, Game.WIDTH - 8, Game.HEIGHT - 8 - 8);
            enterBounds = new Rectangle(0, 0, Game.WIDTH - 24, Game.HEIGHT - 8 - 24);
            monsters = new Array<Entity>();
        }

        public Room(Dungeon map) {
            this(map, 0, 0, 0, 0);
        }

        public void update() {
            // Remove dead monsters from tracking list
            for (Entity e : map.entities.entities) {
                if (monsters.contains(e, true) && e.isDead() && !e.isHit()) {
                    monsters.removeValue(e, true);
                }
            }

            if (!containsPlayer && containsPlayer()) {
                containsPlayer = true;
            }

            // Close door behind player
            Array<Room> close = new Array<Room>();

            close.add(this);
            close.addAll(children);

            for (Room r : close) {
                if (r.doorToParent != null) {
                    if (containsPlayer && monsters.size > 0 && !r.doorToParent.visible) {
                        r.doorToParent.visible = true;
                    } else if (containsPlayer && monsters.size == 0 && r.doorToParent.visible
                            && (r.doorToParent.lock == null || !r.doorToParent.lock.isLocked())) {
                        r.doorToParent.visible = false;
                    }
                }
            }
        }

        public int getTileX() {
            return (int) (rect.x * 16);
        }

        public int getTileY() {
            return (int) (rect.y * 8);
        }

        public int getTileWidth() {
            return (int) (rect.width * 16);
        }

        public int getTileHeight() {
            return (int) (rect.height * 8);
        }

        public int getCenterX() {
            return getTileX() + getTileWidth() / 2;
        }

        public int getCenterY() {
            return getTileY() + getTileHeight() / 2;
        }

        public int x0() {
            return getTileX();
        }

        public int x1() {
            return getTileX() + getTileWidth();
        }

        public int y0() {
            return getTileY();
        }

        public int y1() {
            return getTileY() + getTileHeight();
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

        public Rectangle getBounds() {
            return bounds.setPosition(x0() * 8 + 4, y0() * 8 + 4);
        }

        public Rectangle getEnterBounds() {
            return enterBounds.setPosition(x0() * 8 + 12, y0() * 8 + 12);
        }

        public boolean containsPlayer() {
            return map.player.hitbox.overlaps(getEnterBounds());
        }
    }

    public static class Template implements Serializable {
        public enum Flip {
            HORIZONTAL, VERTICAL, BOTH
        }

        public Flip flip;
        public char[][] template;

        public Template(JsonValue json) {
            deserialize(json);
        }

        public char[][] getTemplate() {
            char[][] template = new char[this.template.length][this.template[0].length];

            // Copy the template
            for (int x = 0; x < template.length; x++) {
                System.arraycopy(this.template[x], 0, template[x], 0, template[0].length);
            }

            // Flip it
            if (flip != null) {
                boolean horizontal = false;
                boolean vertical = false;

                switch (flip) {
                    case HORIZONTAL:
                        if (Game.RANDOM.nextFloat() < .5f) {
                            horizontal = true;
                        }

                        break;
                    case VERTICAL:
                        if (Game.RANDOM.nextFloat() < .5f) {
                            vertical = true;
                        }

                        break;
                    case BOTH:
                        if (Game.RANDOM.nextFloat() < .5f) {
                            horizontal = true;
                        }

                        if (Game.RANDOM.nextFloat() < .5f) {
                            vertical = true;
                        }

                        break;
                }

                if (horizontal) {
                    for (int x = 0; x < template.length / 2; x++) {
                        char[] temp = template[x];

                        template[x] = template[template.length - 1 - x];
                        template[template.length - 1 - x] = temp;
                    }
                }

                if (vertical) {
                    for (int x = 0; x < template.length; x++) {
                        for (int y = 0; y < template[0].length / 2; y++) {
                            char temp = template[x][y];

                            template[x][y] = template[x][template[0].length - 1 - y];
                            template[x][template[0].length - 1 - y] = temp;
                        }
                    }
                }
            }

            return template;
        }

        @Override
        public void deserialize(JsonValue json) {
            JsonValue flip = json.get("flip");
            if (flip != null) {
                this.flip = Flip.valueOf(flip.asString());
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

    public int[] roomCount;

    public String ground;
    public String outerWall;
    public String innerWall;

    public String door;

    public String[] monsters;

    public String key;

    public String[] locks;

    public float lockChance;
    public float killMonsterChance;
    public float keyChance;
    public float pushBlockChance;

    public Template[] templates;

    public Array<Room> rooms;

    @Override
    public void update() {
        super.update();

        if (getCurrentRoom() != null) {
            getCurrentRoom().update();
        }
    }

    @Override
    public void generate() {
        super.generate();

        // Generate parent layout
        int roomCount = this.roomCount[Game.RANDOM.nextInt(this.roomCount.length)];

        rooms = new Array<Room>();

        rooms.add(new Room(this, Game.RANDOM.nextInt(WIDTH), Game.RANDOM.nextInt(HEIGHT), 1, 1));

        while (true) {
            int size = rooms.size;
            boolean done = false;

            for (int i = 0; i < size; i++) {
                if (rooms.size >= roomCount) {
                    done = true;
                    break;
                }

                Room r = rooms.get(i);

                Room room = new Room(this);

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
        entities.clear();

        // Apply templates
        for (Room r : rooms) {
            char[][] template = templates[Game.RANDOM.nextInt(templates.length)].getTemplate();

            for (int x = 0; x < template.length; x++) {
                for (int y = 0; y < template[0].length; y++) {
                    int xx = r.x0() + x;
                    int yy = r.y0() + y;

                    switch (template[x][y]) {
                        case '#':
                            tiles.set(Game.TILES.load(outerWall), xx, yy, 0);
                            break;
                        case '+':
                            tiles.set(Game.TILES.load(innerWall), xx, yy, 0);
                            break;
                        case ' ':
                            tiles.set(Game.TILES.load(ground), xx, yy, 0);
                            break;
                    }
                }
            }
        }

        // Connect parent rooms with its children
        for (int i = 0; i < rooms.size; i++) {
            Room r0 = rooms.get(i);

            for (int j = 0; j < r0.children.size; j++) {
                Room r1 = r0.children.get(j);

                if (r1.x0() != r0.x0()) {
                    if (r1.x0() > r0.x0()) {
                        for (float p = 0; p < 1; p += .01f) {
                            int x = (int) MathUtils.lerp(r0.x1(), r1.x0(), p);

                            for (int xx = -1; xx <= 0; xx++) {
                                for (int yy = -1; yy <= 0; yy++) {
                                    int xxx = x + xx;
                                    int yyy = r0.getCenterY() + yy;

                                    Tile t = tiles.at(xxx, yyy, 0);

                                    if (t != null && t.name.equals(outerWall)) {
                                        tiles.set(Game.TILES.load(ground), xxx, yyy, 0);
                                    }
                                }
                            }
                        }
                    } else {
                        for (float p = 0; p < 1; p += .01f) {
                            int x = (int) MathUtils.lerp(r1.x1(), r0.x0(), p);

                            for (int xx = -1; xx <= 0; xx++) {
                                for (int yy = -1; yy <= 0; yy++) {
                                    int xxx = x + xx;
                                    int yyy = r0.getCenterY() + yy;

                                    Tile t = tiles.at(xxx, yyy, 0);

                                    if (t != null && t.name.equals(outerWall)) {
                                        tiles.set(Game.TILES.load(ground), xxx, yyy, 0);
                                    }
                                }
                            }
                        }
                    }
                }

                if (r1.y0() != r0.y0()) {
                    if (r1.y0() > r0.y0()) {
                        for (float p = 0; p < 1; p += .01f) {
                            int y = (int) MathUtils.lerp(r0.y1(), r1.y0(), p);

                            for (int xx = -1; xx <= 0; xx++) {
                                for (int yy = -1; yy <= 0; yy++) {
                                    int xxx = r0.getCenterX() + xx;
                                    int yyy = y + yy;

                                    Tile t = tiles.at(xxx, yyy, 0);

                                    if (t != null && t.name.equals(outerWall)) {
                                        tiles.set(Game.TILES.load(ground), xxx, yyy, 0);
                                    }
                                }
                            }
                        }
                    } else {
                        for (float p = 0; p < 1; p += .01f) {
                            int y = (int) MathUtils.lerp(r1.y1(), r0.y0(), p);

                            for (int xx = -1; xx <= 0; xx++) {
                                for (int yy = -1; yy <= 0; yy++) {
                                    int xxx = r0.getCenterX() + xx;
                                    int yyy = y + yy;

                                    Tile t = tiles.at(xxx, yyy, 0);

                                    if (t != null && t.name.equals(outerWall)) {
                                        tiles.set(Game.TILES.load(ground), xxx, yyy, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Place player
        Room first = rooms.first();

        Humanoid player = (Humanoid) Game.ENTITIES.load("Human");
        player.x = first.getCenterX() * 8 - 4;
        player.y = first.getCenterY() * 8 - 4;

        player.armor = (Armor) Game.ITEMS.load("Armor");

        player.mainHand = (MainHand) Game.ITEMS.load("BattleAxe");
        player.offHand = (OffHand) Game.ITEMS.load("Shield");

        player.controlledByPlayer = true;

        this.player = player;
        entities.addEntity(player);

        /*
        Dragon dragon = (Dragon) Game.ENTITIES.load("Dragon");

        dragon.x = first.getCenterX() * 8 - 8;
        dragon.y = first.getCenterY() * 8 - 4 + 16;

        entities.addEntity(dragon);
        */

        // Place monsters randomly
        for (Room r : rooms) {
            for (int i = 0; i < 1 + Game.RANDOM.nextInt(4); i++) {
                Entity monster = Game.ENTITIES.load(monsters[Game.RANDOM.nextInt(monsters.length)]);

                int x;
                int y;

                do {
                    x = r.getTileX() + 1 + Game.RANDOM.nextInt(r.getTileWidth() - 2);
                    y = r.getTileY() + 1 + Game.RANDOM.nextInt(r.getTileHeight() - 2);
                } while (tiles.at(x, y, 0) != null && !tiles.at(x, y, 0).name.equals(ground));

                monster.x = x * 8;
                monster.y = y * 8;

                r.monsters.add(monster);

                entities.addEntity(monster);
            }
        }

        // Add doors and lock them
        for (Room r : rooms) {
            for (Room child : r.children) {
                Door d = (Door) Game.ENTITIES.load(door);

                if (child.x0() != r.x0()) {
                    d.y = r.getCenterY() * 8 - 8;
                    d.horizontal = true;

                    if (child.x0() > r.x0()) {
                        d.x = r.x1() * 8 - 8;
                    } else {
                        d.x = r.x0() * 8 - 8;
                    }
                }

                if (child.y0() != r.y0()) {
                    d.x = r.getCenterX() * 8 - 8;

                    if (child.y0() > r.y0()) {
                        d.y = r.y1() * 8 - 8;
                    } else {
                        d.y = r.y0() * 8 - 8;
                    }
                }

                child.doorToParent = d;

                entities.addEntity(d);

                if (Game.RANDOM.nextFloat() < lockChance) {
                    Lock l = Lock.Type.valueOf(locks[Game.RANDOM.nextInt(locks.length)]).newInstance();

                    l.dungeon = this;

                    l.door = d;
                    d.lock = l;

                    Array<Room> rooms = getRoomsUntil(child);
                    if (l.takeSameRoom()) {
                        l.room = r;
                    } else {
                        l.room = rooms.get(Game.RANDOM.nextInt(rooms.size));
                    }

                    l.onLock();
                }
            }
        }
    }

    @Override
    public void placePlayer() {

    }

    public Room getCurrentRoom() {
        for (Room r : rooms) {
            if (player.hitbox.overlaps(r.getBounds())) {
                return r;
            }
        }

        return null;
    }

    public Array<Room> getRoomsUntil(Room stop) {
        Array<Room> rooms = new Array<Room>();
        getRoomsUntil(this.rooms.first(), stop, rooms);
        return rooms;
    }

    public void getRoomsUntil(Room room, Room stop, Array<Room> rooms) {
        rooms.add(room);

        for (int i = 0; i < room.children.size; i++) {
            Room child = room.children.get(i);

            // Stop at specified room or when room already locked off
            if (child != stop && (child.doorToParent != null && child.doorToParent.lock == null)) {
                rooms.add(child);
                getRoomsUntil(child, stop, rooms);
            }
        }
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue roomCount = json.get("roomCount");
        if (roomCount != null) {
            this.roomCount = roomCount.asIntArray();
        }

        JsonValue ground = json.get("ground");
        if (ground != null) {
            this.ground = ground.asString();
        }

        JsonValue outerWall = json.get("outerWall");
        if (outerWall != null) {
            this.outerWall = outerWall.asString();
        }

        JsonValue innerWall = json.get("innerWall");
        if (innerWall != null) {
            this.innerWall = innerWall.asString();
        }

        JsonValue door = json.get("door");
        if (door != null) {
            this.door = door.asString();
        }

        JsonValue monsters = json.get("monsters");
        if (monsters != null) {
            this.monsters = monsters.asStringArray();
        }

        JsonValue key = json.get("key");
        if (key != null) {
            this.key = key.asString();
        }

        JsonValue locks = json.get("locks");
        if (locks != null) {
            this.locks = locks.asStringArray();
        }

        JsonValue lockChance = json.get("lockChance");
        if (lockChance != null) {
            this.lockChance = lockChance.asFloat();
        }

        JsonValue killMonsterChance = json.get("killMonsterChance");
        if (killMonsterChance != null) {
            this.killMonsterChance = killMonsterChance.asFloat();
        }

        JsonValue keyChance = json.get("keyChance");
        if (keyChance != null) {
            this.keyChance = keyChance.asFloat();
        }

        JsonValue pushBlockChance = json.get("pushBlockChance");
        if (pushBlockChance != null) {
            this.pushBlockChance = pushBlockChance.asFloat();
        }

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
