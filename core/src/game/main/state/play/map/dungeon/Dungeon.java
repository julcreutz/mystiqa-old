package game.main.state.play.map.dungeon;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.object.entity.Humanoid;
import game.main.state.play.map.Map;
import game.main.state.play.map.dungeon.lock.Lock;
import game.main.object.entity.*;
import game.main.object.tile.Tile;

public class Dungeon extends Map {
    public static class Room {
        public enum Direction {
            RIGHT, UP, LEFT, DOWN
        }

        public Dungeon map;

        public Rectangle rect;

        public Room parent;
        public Array<Room> children;

        public Door doorToParent;

        public Rectangle outerBounds;
        public Rectangle innerBounds;

        public boolean containsPlayer;

        public float difficulty;

        public Room unlocks;
        public Lock lock;

        public boolean spawnMonsters;

        public Template template;

        public Room(Dungeon map, int x, int y, int w, int h) {
            this.map = map;

            this.rect = new Rectangle(x, y, w, h);
            children = new Array<Room>();

            outerBounds = new Rectangle(0, 0, Game.WIDTH - 8, Game.HEIGHT - 8 - 8);
            innerBounds = new Rectangle(0, 0, Game.WIDTH - 24, Game.HEIGHT - 8 - 24);

            spawnMonsters = true;
        }

        public Room(Dungeon map) {
            this(map, 0, 0, 0, 0);
        }

        public void update() {
            if (!containsPlayer && map.player.getHitbox().overlaps(getInnerBounds())) {
                containsPlayer = true;
            }

            // Close all doors in room the player currently is in
            Array<Room> close = new Array<Room>();

            close.add(this);
            close.addAll(children);

            for (Room r : close) {
                if (r.doorToParent != null) {
                    if (containsPlayer && getMonsterCount() > 0 && !r.doorToParent.visible) {
                        r.doorToParent.visible = true;
                    } else if (containsPlayer && getMonsterCount() == 0 && r.doorToParent.visible
                            && (r.doorToParent.lock == null || !r.doorToParent.lock.isLocked())) {
                        r.doorToParent.visible = false;
                    }
                }
            }

            for (Entity e : getMonsters()) {
                e.x = MathUtils.clamp(e.x, x0() * 8, (x1() - 1) * 8);
                e.y = MathUtils.clamp(e.y, y0() * 8, (y1() - 1) * 8);
            }
        }

        public int getTileX() {
            return (int) (rect.x * 10);
        }

        public int getTileY() {
            return (int) (rect.y * 8);
        }

        public int getTileWidth() {
            return (int) (rect.width * 10);
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

        public Rectangle getOuterBounds() {
            return outerBounds.setPosition(x0() * 8 + 4, y0() * 8 + 4);
        }

        public Rectangle getInnerBounds() {
            return innerBounds.setPosition(x0() * 8 + 12, y0() * 8 + 12);
        }

        public Array<Entity> getContainedEntities() {
            Array<Entity> entities = new Array<Entity>();

            for (Entity e : map.entities.entities) {
                if (e.getHitbox().overlaps(getOuterBounds())) {
                    entities.add(e);
                }
            }

            return entities;
        }

        public Array<Entity> getMonsters() {
            Array<Entity> monsters = new Array<Entity>();

            for (Entity e : getContainedEntities()) {
                if (e.isMonster && e.isVulnerable()) {
                    monsters.add(e);
                }
            }

            return monsters;
        }

        public int getMonsterCount() {
            return getMonsters().size;
        }

        public Array<Direction> getDirections() {
            Array<Direction> directions = new Array<Direction>();

            Array<Room> check = new Array<Room>();

            if (parent != null) {
                check.add(parent);
            }

            if (children.size > 0) {
                check.addAll(children);
            }

            for (Room r : check) {
                if (r.getTileX() > getTileX() && !directions.contains(Direction.RIGHT, true)) {
                    directions.add(Direction.RIGHT);
                }

                if (r.getTileY() > getTileY() && !directions.contains(Direction.UP, true)) {
                    directions.add(Direction.UP);
                }

                if (r.getTileX() < getTileX() && !directions.contains(Direction.LEFT, true)) {
                    directions.add(Direction.LEFT);
                }

                if (r.getTileY() < getTileY() && !directions.contains(Direction.DOWN, true)) {
                    directions.add(Direction.DOWN);
                }
            }

            return directions;
        }

        public boolean isBossRoom() {
            return template == map.bossTemplate;
        }
    }

    public static class Template implements Serializable {
        public float minChance;
        public float maxChance;

        public float chance;

        public Array<Room.Direction> forbiddenDirections;

        public char[][] template;

        public Template(JsonValue json) {
            minChance = 1;
            maxChance = 1;

            chance = 1;

            deserialize(json);
        }

        public boolean isForbidden(Room r) {
            if (forbiddenDirections == null) {
                return false;
            }

            Array<Room.Direction> directions = r.getDirections();

            for (Room.Direction direction : directions) {
                if (forbiddenDirections.contains(direction, true)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public void deserialize(JsonValue json) {
            JsonValue minChance = json.get("minChance");
            if (minChance != null) {
                this.minChance = minChance.asFloat();
            }

            JsonValue maxChance = json.get("maxChance");
            if (maxChance != null) {
                this.maxChance = maxChance.asFloat();
            }

            JsonValue chance = json.get("chance");
            if (chance != null) {
                this.chance = chance.asFloat();
            }

            JsonValue forbiddenDirections = json.get("forbiddenDirections");
            if (forbiddenDirections != null) {
                this.forbiddenDirections = new Array<Room.Direction>();

                for (JsonValue direction : forbiddenDirections) {
                    this.forbiddenDirections.add(Room.Direction.valueOf(direction.asString()));
                }
            }

            JsonValue template = json.get("template");
            if (template != null) {
                this.template = new char[10][8];

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

    public static class Monster implements Serializable {
        public String monster;

        public float minChance;
        public float maxChance;

        public Monster(JsonValue json) {
            deserialize(json);
        }

        @Override
        public void deserialize(JsonValue json) {
            JsonValue monster = json.get("monster");
            if (monster != null) {
                this.monster = monster.asString();
            }

            JsonValue minChance = json.get("minChance");
            if (minChance != null) {
                this.minChance = minChance.asFloat();
            }

            JsonValue maxChance = json.get("maxChance");
            if (maxChance != null) {
                this.maxChance = maxChance.asFloat();
            }
        }
    }

    public static class LockChoice implements Serializable {
        public Lock.Type lock;

        public float minChance;
        public float maxChance;

        public LockChoice(JsonValue json) {
            deserialize(json);
        }

        @Override
        public void deserialize(JsonValue json) {
            JsonValue lock = json.get("lock");
            if (lock != null) {
                this.lock = Lock.Type.valueOf(lock.asString());
            }

            JsonValue minChance = json.get("minChance");
            if (minChance != null) {
                this.minChance = minChance.asFloat();
            }

            JsonValue maxChance = json.get("maxChance");
            if (maxChance != null) {
                this.maxChance = maxChance.asFloat();
            }
        }
    }

    public static final int WIDTH = 16;
    public static final int HEIGHT = 8;

    public int minRooms;
    public int maxRooms;

    public String ground;
    public String outerWall;
    public String innerWall;
    public String hole;

    public String door;
    public String block;
    public String spikes;
    public String chest;

    public int minTreasureRooms;
    public int maxTreasureRooms;

    public int minMonsters;
    public int maxMonsters;

    public Monster[] monsters;

    public String boss;

    public String key;
    public String bossKey;

    public Array<LockChoice> locks;

    public Template bossTemplate;
    public Template[] templates;

    public Array<Room> rooms;

    public Array<Lock> allLocks;

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
        int roomCount = minRooms + Game.RANDOM.nextInt(maxRooms - minRooms + 1);

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

                        room.difficulty = r.difficulty + Game.RANDOM.nextFloat();

                        rooms.add(room);
                    }
                }
            }

            if (done) {
                break;
            }
        }

        // Extra difficulty rules
        for (Room r : rooms) {
            if (r.children.size == 0) {
                r.difficulty *= 2;
            }
        }

        // Normalize difficulty
        float maxDifficulty = 0;

        for (Room r : rooms) {
            if (r.difficulty > maxDifficulty) {
                maxDifficulty = r.difficulty;
            }
        }

        for (Room r : rooms) {
            r.difficulty /= maxDifficulty;
        }

        // Pick the boss room
        Room bossRoom = null;

        if (this.bossTemplate != null) {
            Array<Room> possible = new Array<Room>();

            for (Room r : rooms) {
                if (!this.bossTemplate.isForbidden(r)) {
                    possible.add(r);
                }
            }

            if (possible.size > 0) {
                bossRoom = possible.first();

                for (Room r : possible) {
                    if (r.difficulty > bossRoom.difficulty) {
                        bossRoom = r;
                    }
                }

                bossRoom.template = this.bossTemplate;
                bossRoom.spawnMonsters = false;
            }
        }

        tiles.initSize(WIDTH * 16, HEIGHT * 8, 8);
        entities.clear();

        // Add doors
        allLocks = new Array<Lock>();

        if (door != null) {
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
                }
            }

            // Init and lock boss room
            if (bossRoom != null) {
                lock(bossRoom, bossRoom, bossRoom.doorToParent, Lock.Type.BOSS_KEY);
            }

            // Lock random rooms
            for (Room r : rooms) {
                for (Room child : r.children) {
                    // Only lock if not already locked
                    if (child.doorToParent.lock == null) {
                        if (locks.size > 0) {
                            lock(r, child, child.doorToParent, getRandomLock(child));
                        }
                    }
                }
            }
        }

        // Apply templates
        for (Room r : rooms) {
            Array<Template> templates = new Array<Template>();

            while (templates.size == 0) {
                for (Template t : this.templates) {
                    if (Game.RANDOM.nextFloat() < MathUtils.lerp(t.minChance, t.maxChance, r.difficulty)
                            && Game.RANDOM.nextFloat() < t.chance && (r.lock == null || r.lock.isValid(t))
                            && !t.isForbidden(r)) {
                        templates.add(t);
                    }
                }
            }

            char[][] template;

            if (r.template != null) {
                template = r.template.template;
            } else {
                template = templates.get(Game.RANDOM.nextInt(templates.size)).template;
            }

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
                        case '.':
                            tiles.set(Game.TILES.load(hole), xx, yy, 0);
                            break;
                        case 'b':
                            tiles.set(Game.TILES.load(ground), xx, yy, 0);

                            Entity boss = Game.ENTITIES.load(this.boss);

                            boss.x = xx * 8;
                            boss.y = yy * 8;

                            entities.addEntity(boss);

                            break;
                        case 'p':
                            if (r.unlocks == null) {
                                tiles.set(Game.TILES.load(block), xx, yy, 0);
                            } else {
                                tiles.set(Game.TILES.load(ground), xx, yy, 0);

                                Block b = (Block) Game.ENTITIES.load(block);

                                b.x = xx * 8;
                                b.y = yy * 8;

                                entities.addEntity(b);
                            }

                            break;
                        case 'c':
                            tiles.set(Game.TILES.load(ground), xx, yy, 0);

                            if (Game.RANDOM.nextFloat() < r.difficulty) {
                                Chest c = (Chest) Game.ENTITIES.load(chest);

                                c.x = xx * 8;
                                c.y = yy * 8;

                                entities.addEntity(c);
                            }

                            break;
                        case '0':
                        case '1':
                        case '2':
                        case '3':
                        case '4':
                        case '5':
                        case '6':
                        case '7':
                        case '8':
                        case '9':
                            tiles.set(Game.TILES.load(ground), xx, yy, 0);

                            Spikes s = (Spikes) Game.ENTITIES.load(spikes);

                            s.x = xx * 8;
                            s.y = yy * 8;

                            s.offset = Integer.parseInt("" + template[x][y]) * .1f;

                            entities.addEntity(s);

                            break;
                    }
                }
            }
        }

        // Close off rooms if not connected to respective direction
        for (Room r : rooms) {
            Array<Room.Direction> directions = r.getDirections();

            if (!directions.contains(Room.Direction.RIGHT, true)) {
                for (int y = r.y0(); y < r.y1(); y++) {
                    tiles.set(Game.TILES.load(outerWall), r.x1() - 1, y, 0);
                }
            }

            if (!directions.contains(Room.Direction.LEFT, true)) {
                for (int y = r.y0(); y < r.y1(); y++) {
                    tiles.set(Game.TILES.load(outerWall), r.x0(), y, 0);
                }
            }

            if (!directions.contains(Room.Direction.UP, true)) {
                for (int x = r.x0(); x < r.x1(); x++) {
                    tiles.set(Game.TILES.load(outerWall), x, r.y1() - 1, 0);
                }
            }

            if (!directions.contains(Room.Direction.DOWN, true)) {
                for (int x = r.x0(); x < r.x1(); x++) {
                    tiles.set(Game.TILES.load(outerWall), x, r.y0(), 0);
                }
            }
        }

        // Extend room connections to make sure they are traversable
        for (Room r : rooms) {
            Array<Room.Direction> directions = r.getDirections();

            if (directions.contains(Room.Direction.RIGHT, true)) {
                int x = r.x1() - 1;

                for (int y = r.y0(); y < r.y1(); y++) {
                    if (tiles.at(x, y, 0).id.equals(ground)) {
                        tiles.set(Game.TILES.load(ground), x + 1, y, 0);
                    }
                }
            }

            if (directions.contains(Room.Direction.LEFT, true)) {
                int x = r.x0();

                for (int y = r.y0(); y < r.y1(); y++) {
                    if (tiles.at(x, y, 0).id.equals(ground)) {
                        tiles.set(Game.TILES.load(ground), x - 1, y, 0);
                    }
                }
            }

            if (directions.contains(Room.Direction.UP, true)) {
                int y = r.y1() - 1;

                for (int x = r.x0(); x < r.x1(); x++) {
                    if (tiles.at(x, y, 0).id.equals(ground)) {
                        tiles.set(Game.TILES.load(ground), x, y + 1, 0);
                    }
                }
            }

            if (directions.contains(Room.Direction.DOWN, true)) {
                int y = r.y0();

                for (int x = r.x0(); x < r.x1(); x++) {
                    if (tiles.at(x, y, 0).id.equals(ground)) {
                        tiles.set(Game.TILES.load(ground), x, y - 1, 0);
                    }
                }
            }
        }

        /*
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

                                    if (t != null && t.id.equals(outerWall)) {
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

                                    if (t != null && t.id.equals(outerWall)) {
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

                                    if (t != null && t.id.equals(outerWall)) {
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

                                    if (t != null && t.id.equals(outerWall)) {
                                        tiles.set(Game.TILES.load(ground), xxx, yyy, 0);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        */

        // Place player
        rooms.first().spawnMonsters = false;

        Humanoid player = (Humanoid) Game.ENTITIES.load("Player");
        player.x = rooms.first().getCenterX() * 8 - 4;
        player.y = rooms.first().getCenterY() * 8 - 4;

        player.controlledByPlayer = true;

        this.player = player;
        entities.addEntity(player);

        // Place monsters randomly
        for (Room r : rooms) {
            if (r.spawnMonsters) {
                int monsterCount = (int) MathUtils.lerp(minMonsters, maxMonsters, r.difficulty);

                while (r.getMonsterCount() < monsterCount) {
                    Array<String> monsters = new Array<String>();

                    for (Monster m : this.monsters) {
                        if (Game.RANDOM.nextFloat() < MathUtils.lerp(m.minChance, m.maxChance, r.difficulty)) {
                            monsters.add(m.monster);
                        }
                    }

                    if (monsters.size > 0) {
                        Entity monster = Game.ENTITIES.load(monsters.get(Game.RANDOM.nextInt(monsters.size)));

                        int x;
                        int y;

                        do {
                            x = r.getTileX() + 1 + Game.RANDOM.nextInt(r.getTileWidth() - 2);
                            y = r.getTileY() + 1 + Game.RANDOM.nextInt(r.getTileHeight() - 2);
                        } while (tiles.at(x, y, 0) != null && !tiles.at(x, y, 0).id.equals(ground));

                        monster.x = x * 8;
                        monster.y = y * 8;

                        entities.addEntity(monster);
                    }
                }
            }
        }

        // Lock the doors
        for (Lock l : allLocks) {
            l.onLock();
        }
    }

    @Override
    public void placePlayer() {

    }

    public Room getCurrentRoom() {
        for (Room r : rooms) {
            if (player.hitbox.overlaps(r.getOuterBounds())) {
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

    public void lock(Room r, Room child, Door d, Lock l) {
        l.dungeon = this;

        Array<Room> valid = new Array<Room>();

        // Get valid rooms and make sure room has only one room it unlocks
        for (Room room : getRoomsUntil(child)) {
            if (room != rooms.first() && !room.isBossRoom() && room.unlocks == null
                    && room.spawnMonsters) {
                valid.add(room);
            }
        }

        if (valid.size > 0) {
            // Pick room with highest difficulty
            l.room = valid.first();

            for (Room room : valid) {
                if (room.difficulty > l.room.difficulty) {
                    l.room = room;
                }
            }

            l.room.unlocks = r;

            l.room.lock = l;

            l.door = d;
            d.lock = l;

            entities.addListener(l);

            allLocks.add(l);
        }
    }

    public void lock(Room r, Room child, Door d, Lock.Type lock) {
        lock(r, child, d, lock.newInstance());
    }

    public Lock.Type getRandomLock(Room r) {
        Array<Lock.Type> locks = new Array<Lock.Type>();

        // Pick random lock; if null, don't lock door
        while (locks.size == 0) {
            for (LockChoice lockChoice : this.locks) {
                float chance = MathUtils.lerp(lockChoice.minChance, lockChoice.maxChance, r.difficulty);

                if (Game.RANDOM.nextFloat() < chance) {
                    locks.add(lockChoice.lock);
                }
            }
        }

        return locks.get(Game.RANDOM.nextInt(locks.size));
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue minRooms = json.get("minRooms");
        if (minRooms != null) {
            this.minRooms = minRooms.asInt();
        }

        JsonValue maxRooms = json.get("maxRooms");
        if (maxRooms != null) {
            this.maxRooms = maxRooms.asInt();
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

        JsonValue hole = json.get("hole");
        if (hole != null) {
            this.hole = hole.asString();
        }
        
        JsonValue door = json.get("door");
        if (door != null) {
            this.door = door.asString();
        }

        JsonValue block = json.get("block");
        if (block != null) {
            this.block = block.asString();
        }
        
        JsonValue spikes = json.get("spikes");
        if (spikes != null) {
            this.spikes = spikes.asString();
        }

        JsonValue chest = json.get("chest");
        if (chest != null) {
            this.chest = chest.asString();
        }

        JsonValue minTreasureRooms = json.get("minTreasureRooms");
        if (minTreasureRooms != null) {
            this.minTreasureRooms = minTreasureRooms.asInt();
        }

        JsonValue maxTreasureRooms = json.get("maxTreasureRooms");
        if (maxTreasureRooms != null) {
            this.maxTreasureRooms = maxTreasureRooms.asInt();
        }

        JsonValue minMonsters = json.get("minMonsters");
        if (minMonsters != null) {
            this.minMonsters = minMonsters.asInt();
        }

        JsonValue maxMonsters = json.get("maxMonsters");
        if (maxMonsters != null) {
            this.maxMonsters = maxMonsters.asInt();
        }

        JsonValue monsters = json.get("monsters");
        if (monsters != null) {
            this.monsters = new Monster[monsters.size];

            for (int i = 0; i < monsters.size; i++) {
                this.monsters[i] = new Monster(monsters.get(i));
            }
        }

        JsonValue boss = json.get("boss");
        if (boss != null) {
            this.boss = boss.asString();
        }

        JsonValue key = json.get("key");
        if (key != null) {
            this.key = key.asString();
        }

        JsonValue bossKey = json.get("bossKey");
        if (bossKey != null) {
            this.bossKey = bossKey.asString();
        }

        JsonValue locks = json.get("locks");
        if (locks != null) {
            this.locks = new Array<LockChoice>();

            for (JsonValue lock : locks) {
                this.locks.add(new LockChoice(lock));
            }
        }

        JsonValue bossTemplate = json.get("bossTemplate");
        if (bossTemplate != null) {
            this.bossTemplate = new Template(bossTemplate);
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
