package game.main.state.play.map.dungeon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectFloatMap;
import game.loader.Serializable;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.object.item.equipment.armor.Armor;
import game.main.object.item.equipment.hand.right.MeleeWeapon;
import game.main.object.item.equipment.hand.right.RightHand;
import game.main.object.item.equipment.hand.left.LeftHand;
import game.main.state.play.map.Map;
import game.main.state.play.map.dungeon.lock.Lock;
import game.main.object.entity.*;
import game.main.object.tile.Tile;

public class Dungeon extends Map {
    public static class Room {
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

        public boolean isBossRoom;
        public boolean isTreasureRoom;

        public boolean spawnMonsters;

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
                if (e.isMonster) {
                    monsters.add(e);
                }
            }

            return monsters;
        }

        public int getMonsterCount() {
            return getMonsters().size;
        }
    }

    public static class Template implements Serializable {
        public boolean horizontallyFlippable;
        public boolean verticallyFlippable;

        public boolean isBossRoom;
        public boolean isTreasureRoom;

        public float minDifficulty;
        public float maxDifficulty;

        public float chance;

        public char[][] template;

        public Template(JsonValue json) {
            minDifficulty = 0;
            maxDifficulty = 1;

            chance = 1;

            deserialize(json);
        }

        public char[][] getTemplate() {
            char[][] template = new char[this.template.length][this.template[0].length];

            // Copy the template
            for (int x = 0; x < template.length; x++) {
                System.arraycopy(this.template[x], 0, template[x], 0, template[0].length);
            }

            // Flip it
            if (horizontallyFlippable && Game.RANDOM.nextFloat() < .5f) {
                for (int x = 0; x < template.length / 2; x++) {
                    char[] temp = template[x];

                    template[x] = template[template.length - 1 - x];
                    template[template.length - 1 - x] = temp;
                }
            }

            if (verticallyFlippable && Game.RANDOM.nextFloat() < .5f) {
                for (int x = 0; x < template.length; x++) {
                    for (int y = 0; y < template[0].length / 2; y++) {
                        char temp = template[x][y];

                        template[x][y] = template[x][template[0].length - 1 - y];
                        template[x][template[0].length - 1 - y] = temp;
                    }
                }
            }

            return template;
        }

        @Override
        public void deserialize(JsonValue json) {
            JsonValue horizontallyFlippable = json.get("horizontallyFlippable");
            if (horizontallyFlippable != null) {
                this.horizontallyFlippable = horizontallyFlippable.asBoolean();
            }
            
            JsonValue verticallyFlippable = json.get("verticallyFlippable");
            if (verticallyFlippable != null) {
                this.verticallyFlippable = verticallyFlippable.asBoolean();
            }

            JsonValue isBossRoom = json.get("isBossRoom");
            if (isBossRoom != null) {
                this.isBossRoom = isBossRoom.asBoolean();
            }

            JsonValue isTreasureRoom = json.get("isTreasureRoom");
            if (isTreasureRoom != null) {
                this.isTreasureRoom = isTreasureRoom.asBoolean();
            }

            JsonValue minDifficulty = json.get("minDifficulty");
            if (minDifficulty != null) {
                this.minDifficulty = minDifficulty.asFloat();
            }

            JsonValue maxDifficulty = json.get("maxDifficulty");
            if (maxDifficulty != null) {
                this.maxDifficulty = maxDifficulty.asFloat();
            }

            JsonValue chance = json.get("chance");
            if (chance != null) {
                this.chance = chance.asFloat();
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
        public float minDifficulty;
        public float maxDifficulty;

        public float chance;

        public String monster;

        public Monster(JsonValue json) {
            minDifficulty = 0;
            maxDifficulty = 1;

            chance = 1;

            deserialize(json);
        }

        @Override
        public void deserialize(JsonValue json) {
            JsonValue minDifficulty = json.get("minDifficulty");
            if (minDifficulty != null) {
                this.minDifficulty = minDifficulty.asFloat();
            }

            JsonValue maxDifficulty = json.get("maxDifficulty");
            if (maxDifficulty != null) {
                this.maxDifficulty = maxDifficulty.asFloat();
            }

            JsonValue chance = json.get("chance");
            if (chance != null) {
                this.chance = chance.asFloat();
            }

            JsonValue monster = json.get("monster");
            if (monster != null) {
                this.monster = monster.asString();
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

    public SpriteSheet doorNoKey;
    public SpriteSheet doorKey;
    public SpriteSheet doorWall;

    public SpriteSheet block;

    public float treasureRoomChance;

    public int minMonsters;
    public int maxMonsters;

    public Monster[] monsters;

    public String boss;

    public String key;

    public ObjectFloatMap<String> locks;

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

        // Pick boss room
        Room bossRoom = null;

        for (Room r : rooms) {
            if (r.children.size == 0 && (bossRoom == null || r.difficulty > bossRoom.difficulty)) {
                bossRoom = r;
            }
        }

        tiles.initSize(WIDTH * 16, HEIGHT * 8, 8);
        entities.clear();

        // Add doors
        allLocks = new Array<Lock>();

        for (Room r : rooms) {
            for (Room child : r.children) {
                Door d = new Door();

                d.spriteSheet = doorNoKey;
                d.wall = doorWall;

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

        if (bossRoom != null) {
            bossRoom.isBossRoom = true;
            bossRoom.spawnMonsters = false;
            lock(bossRoom, bossRoom, bossRoom.doorToParent, Lock.Type.KEY);
        }

        for (Room r : rooms) {
            for (Room child : r.children) {
                String lock = null;

                // Pick random lock; if null, don't lock door
                for (String type : locks.keys()) {
                    if (Game.RANDOM.nextFloat() < locks.get(type, 0)) {
                        lock = type;
                    }
                }

                if (lock != null) {
                    lock(r, child, child.doorToParent, Lock.Type.valueOf(lock));
                }
            }
        }

        // Place random treasure rooms
        for (Room r : rooms) {
            if (r.children.size == 0 && r.unlocks == null && !r.isBossRoom
                    && Game.RANDOM.nextFloat() < treasureRoomChance) {
                r.isTreasureRoom = true;
                r.spawnMonsters = false;
            }
        }

        // Apply templates
        for (Room r : rooms) {
            Array<Template> templates = new Array<Template>();

            for (Template t : this.templates) {
                if (r.difficulty >= t.minDifficulty && r.difficulty <= t.maxDifficulty
                        && Game.RANDOM.nextFloat() < t.chance && (r.lock == null || r.lock.isValid(t))
                        && r.isBossRoom == t.isBossRoom && r.isTreasureRoom == t.isTreasureRoom) {
                    templates.add(t);
                }
            }

            char[][] template = templates.get(Game.RANDOM.nextInt(templates.size)).getTemplate();

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
                        case 'b':
                            tiles.set(Game.TILES.load(ground), xx, yy, 0);

                            Entity boss = Game.ENTITIES.load(this.boss);

                            boss.x = xx * 8;
                            boss.y = yy * 8;

                            entities.addEntity(boss);

                            break;
                        case 'p':
                            tiles.set(Game.TILES.load(ground), xx, yy, 0);

                            Block b = new Block();

                            b.spriteSheet = block;

                            b.x = xx * 8;
                            b.y = yy * 8;

                            entities.addEntity(b);

                            break;
                        case 't':
                            tiles.set(Game.TILES.load(ground), xx, yy, 0);

                            Chest c = new Chest();

                            c.x = xx * 8 + 4;
                            c.y = yy * 8 + 4;

                            entities.addEntity(c);

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

        // Place player
        Room first = rooms.first();

        Humanoid player = (Humanoid) Game.ENTITIES.load("Human");
        player.x = first.getCenterX() * 8 - 4;
        player.y = first.getCenterY() * 8 - 4;

        player.armor = (Armor) Game.ITEMS.load("PlateArmor");

        player.rightHand = (RightHand) Game.ITEMS.load("Halberd");
        player.leftHand = (LeftHand) Game.ITEMS.load("Shield");

        player.controlledByPlayer = true;

        this.player = player;
        entities.addEntity(player);

        // Place monsters randomly
        for (Room r : rooms) {
            if (r.spawnMonsters) {
                for (int i = 0; i < MathUtils.lerp(minMonsters, maxMonsters, r.difficulty); i++) {
                    Array<String> monsters = new Array<String>();

                    for (Monster m : this.monsters) {
                        if (r.difficulty >= m.minDifficulty && r.difficulty < m.maxDifficulty
                                && Game.RANDOM.nextFloat() < m.chance) {
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

    public void lock(Room r, Room child, Door d, Lock.Type lock) {
        Lock l = lock.newInstance();

        l.dungeon = this;

        Array<Room> valid = new Array<Room>();

        // Get valid rooms and make sure room has only one room it unlocks
        for (Room room : getRoomsUntil(child)) {
            if (room != rooms.first() && !room.isBossRoom && room.unlocks == null) {
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
        
        JsonValue doorNoKey = json.get("doorNoKey");
        if (doorNoKey != null) {
            this.doorNoKey = Game.SPRITE_SHEETS.load(doorNoKey.asString());
        }

        JsonValue doorKey = json.get("doorKey");
        if (doorKey != null) {
            this.doorKey = Game.SPRITE_SHEETS.load(doorKey.asString());
        }

        JsonValue doorWall = json.get("doorWall");
        if (doorWall != null) {
            this.doorWall = Game.SPRITE_SHEETS.load(doorWall.asString());
        }

        JsonValue block = json.get("block");
        if (block != null) {
            this.block = Game.SPRITE_SHEETS.load(block.asString());
        }

        JsonValue treasureRoomChance = json.get("treasureRoomChance");
        if (treasureRoomChance != null) {
            this.treasureRoomChance = treasureRoomChance.asFloat();
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

        JsonValue locks = json.get("locks");
        if (locks != null) {
            this.locks = new ObjectFloatMap<String>();

            for (JsonValue lock : locks) {
                this.locks.put(lock.child.name, lock.child.asFloat());
            }
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
