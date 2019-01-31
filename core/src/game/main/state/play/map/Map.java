package game.main.state.play.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.SpriteSheet;
import game.main.Game;
import game.main.positionable.entity.*;
import game.main.positionable.entity.monster.Bat;
import game.main.positionable.entity.monster.Monster;
import game.main.state.play.Play;
import game.main.state.play.map.lock.Lock;
import game.main.positionable.tile.Tile;
import game.main.positionable.tile.TileManager;

public abstract class Map {
    public static class Teleport {
        public Rectangle rect;

        public Map destination;

        public float destinationX;
        public float destinationY;

        public Teleport(Map destination, float destinationX, float destinationY, float x, float y, float width, float height) {
            this.destination = destination;

            this.destinationX = destinationX;
            this.destinationY = destinationY;

            rect = new Rectangle(x, y, width, height);
        }
    }

    public static class Node {
        public int x;
        public int y;

        public Node parent;

        public float f;
        public float g;
        public float h;

        public Node(int x, int y, Node parent) {
            this.x = x;
            this.y = y;

            this.parent = parent;
        }

        public Node(int x, int y) {
            this(x, y, null);
        }

        public Node() {
            this(0, 0, null);
        }
    }

    public static class Room {
        public enum Direction {
            RIGHT, UP, LEFT, DOWN
        }

        public Map map;

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

        public Room(Map map, int x, int y, int w, int h) {
            this.map = map;

            this.rect = new Rectangle(x, y, w, h);
            children = new Array<Room>();

            outerBounds = new Rectangle(0, 0, Game.WIDTH - 8, Game.HEIGHT - 8 - 8);
            innerBounds = new Rectangle(0, 0, Game.WIDTH - 24, Game.HEIGHT - 8 - 24);

            spawnMonsters = true;
        }

        public Room(Map map) {
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

            // Keep monsters in room
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
                if (e instanceof Monster) {
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

    public static class Template {
        public float minChance;
        public float maxChance;

        public float chance;

        public Array<Room.Direction> forbiddenDirections;

        public char[][] template;

        public Template(char[][] template, Room.Direction... forbiddenDirections) {
            minChance = 1;
            maxChance = 1;

            chance = 1;

            this.template = template;
            this.forbiddenDirections = new Array<Room.Direction>(forbiddenDirections);
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
    }

    public static class LockChoice {
        public Lock.Type lock;

        public float minChance;
        public float maxChance;
    }

    public static class Ground {
        public float chance;
        public Class tile;

        public Ground(float chance, Class tile) {
            this.chance = chance;
            this.tile = tile;
        }
    }

    public static final float CAM_SPEED = 1.5f;

    public static final int X_VIEW = 6;
    public static final int Y_VIEW = 6;

    public static final int WIDTH = 16;
    public static final int HEIGHT = 8;

    public Play play;

    public SpriteSheet guiLayer;

    public boolean generated;

    public TileManager tiles;

    public EntityManager entities;
    public Player player;

    public int x0;
    public int x1;
    public int y0;
    public int y1;

    public float camX;
    public float camY;

    public float toCamX;
    public float toCamY;

    public float camPosX;
    public float camPosY;

    public float camTime;

    public Array<Teleport> teleports;

    public float screenShake;

    public int minRooms;
    public int maxRooms;

    public Class ground;
    public Class outerWall;
    public Class innerWall;
    public Class hole;

    public Class door;
    public Class block;
    public Class spikes;
    public Class chest;

    public int minTreasureRooms;
    public int maxTreasureRooms;

    public int minMonsters;
    public int maxMonsters;

    public Monster[] monsters;

    public Class boss;

    public Class key;
    public Class bossKey;

    public Array<LockChoice> locks;

    public Template bossTemplate;
    public Template[] templates;

    public Array<Room> rooms;

    public Array<Lock> allLocks;

    public Array<Ground> grounds;

    public Map() {
        guiLayer = new SpriteSheet("gui_layer");

        tiles = new TileManager(this);
        entities = new EntityManager(this);

        teleports = new Array<Teleport>();

        grounds = new Array<Ground>();
    }

    public void update() {
        toCamX = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        toCamY = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / (Game.HEIGHT - 8)) * (Game.HEIGHT - 8);

        if (camX != toCamX || camY != toCamY) {
            if (camTime == 0) {
                camTime = 1;
            }

            camTime -= Game.getDelta() * CAM_SPEED;

            float p = MathUtils.clamp(1 - camTime, 0, 1);

            camPosX = MathUtils.lerp(camX, toCamX, p);
            camPosY = MathUtils.lerp(camY, toCamY, p);

            if (camTime < 0) {
                camX = toCamX;
                camY = toCamY;

                camTime = 0;
            }
        }

        x0 = MathUtils.clamp(MathUtils.floor(play.cam.position.x / 8f) - X_VIEW, 0, tiles.getWidth());
        x1 = MathUtils.clamp(x0 + X_VIEW * 2, 0, tiles.getWidth());
        y0 = MathUtils.clamp(MathUtils.floor(play.cam.position.y / 8f) - Y_VIEW, 0, tiles.getHeight());
        y1 = MathUtils.clamp(y0 + Y_VIEW * 2, 0, tiles.getHeight());

        tiles.update(x0, x1, y0, y1);
        entities.update();

        if (player.onTeleport) {
            boolean onTeleport = false;

            for (Teleport t : teleports) {
                if (t.rect.overlaps(player.hitbox.rect)) {
                    onTeleport = true;
                    break;
                }
            }

            if (!onTeleport) {
                player.onTeleport = false;
            }
        } else {
            for (Teleport t : teleports) {
                if (t.rect.overlaps(player.hitbox.rect)) {
                    player.onTeleport = true;

                    if (!t.destination.generated) {
                        t.destination.generate();
                    }

                    play.nextMap = t.destination;

                    player.x = t.destinationX;
                    player.y = t.destinationY;

                    play.nextMap.entities.addEntity(player);
                    play.nextMap.player = player;

                    entities.entities.removeValue(player, true);
                    player = null;

                    break;
                }
            }
        }

        if (getCurrentRoom() != null) {
            getCurrentRoom().update();
        }
    }

    public void render(SpriteBatch batch) {
        batch.setShader(null);

        tiles.render(batch, x0, x1, y0, y1);

        batch.setShader(null);

        entities.render(batch);

        batch.setShader(null);
    }

    public void positionCamera() {
        camX = toCamX = camPosX = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        camY = toCamY = camPosY = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / (Game.HEIGHT - 8)) * (Game.HEIGHT - 8);
    }

    public boolean isCamMoving() {
        return camX != toCamX || camY != toCamY;
    }

    public boolean isVisible(float x, float y) {
        return x >= x0 * 8 && x < x1 * 8 && y >= y0 * 8 && y < y1 * 8;
    }

    public boolean isVisible(Entity e) {
        return isVisible(e.x, e.y);
    }

    public void connect(Map destination, float fromX, float fromY, float toX, float toY) {
        teleports.add(new Teleport(destination, toX, toY, fromX, fromY, 8, 8));
        destination.teleports.add(new Teleport(this, fromX, fromY, toX, toY, 8, 8));
    }

    public Array<Node> findPath(int x1, int y1, int x2, int y2) {
        Array<Node> openList = new Array<Node>();
        Array<Node> closedList = new Array<Node>();

        openList.add(new Node(x1, y1));

        do {
            Node curr = null;

            // Find node with lowest f
            for (Node node : openList) {
                if (curr == null || node.f < curr.f) {
                    curr = node;
                }
            }

            if (curr == null) {
                return null;
            }

            // Remove from open list to prevent infinite loop
            openList.removeValue(curr, true);

            // Return path when node with lowest f is goal node
            if (curr.x == x2 && curr.y == y2) {
                Array<Node> path = new Array<Node>();

                // Recursively build path
                while (curr != null) {
                    path.add(curr);
                    curr = curr.parent;
                }

                // Reverse to have start at index 0
                path.reverse();

                return path;
            }

            // Add node to closed list to prevent infinite loop
            closedList.add(curr);

            // Check neighbour nodes
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    if ((x != 0 || y != 0) && x * y == 0) {
                        int xx = curr.x + x;
                        int yy = curr.y + y;

                        if (tiles.inBounds(xx, yy)) {
                            Tile t = tiles.at(xx, yy);

                            if (xx >= 0 && xx < tiles.getWidth() && yy >= 0 && yy < tiles.getHeight() && t != null && t.hitbox == null) {
                                Node node = new Node(xx, yy);

                                // Ignore if node is in closed list
                                if (inList(closedList, node)) {
                                    continue;
                                }

                                // Traversal cost of new node + old one
                                float g = curr.g + 1;

                                // Ignore if new path is slower
                                if (inList(openList, node) && g >= nodeAt(openList, xx, yy).g) {
                                    continue;
                                }

                                node.parent = curr;
                                node.g = g;

                                float f = g + (int) new Vector2(xx, yy).sub(x2, y2).len();

                                // Update f or add to open list if not already in it
                                if (inList(openList, node)) {
                                    nodeAt(openList, xx, yy).f = f;
                                } else {
                                    node.f = f;
                                    openList.add(node);
                                }
                            }
                        }
                    }
                }
            }
        } while (openList.size > 0);

        return null;
    }

    public Node nodeAt(Array<Node> list, int x, int y) {
        for (Node node : list) {
            if (node.x == x && node.y == y) {
                return node;
            }
        }

        return null;
    }

    public boolean inList(Array<Node> list, Node node) {
        for (Node _node : list) {
            if (node.x == _node.x && node.y == _node.y) {
                return true;
            }
        }

        return false;
    }

    public void generate() {
        generated = true;

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

        tiles.initSize(WIDTH * 16, HEIGHT * 8);
        entities.clear();

        // Add doors
        allLocks = new Array<Lock>();

        if (door != null) {
            for (Room r : rooms) {
                for (Room child : r.children) {
                    //Door d = (Door) Game.ENTITIES.load(door);
                    Door d = null;

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

            for (int x = 0; x < template[0].length; x++) {
                for (int y = 0; y < template.length; y++) {
                    int xx = r.x0() + x;
                    int yy = r.y0() + y;

                    switch (template[template.length - 1 - y][x]) {
                        case '#':
                            try {
                                tiles.set((Tile) outerWall.newInstance(), xx, yy);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            break;
                        case '+':
                            try {
                                tiles.set((Tile) innerWall.newInstance(), xx, yy);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            break;
                        case ' ':
                            tiles.set(getGround(), xx, yy);
                            break;
                        case '.':
                            try {
                                tiles.set((Tile) hole.newInstance(), xx, yy);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            break;
                        case 'b':
                            tiles.set(getGround(), xx, yy);

                            try {
                                Entity boss = (Entity) this.boss.newInstance();

                                boss.x = xx * 8;
                                boss.y = yy * 8;

                                entities.addEntity(boss);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            break;
                        case 'p':
                            if (r.unlocks == null) {
                                try {
                                    tiles.set((Tile) block.newInstance(), xx, yy);
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                tiles.set(getGround(), xx, yy);

                                try {
                                    Block b = (Block) block.newInstance();

                                    b.x = xx * 8;
                                    b.y = yy * 8;

                                    entities.addEntity(b);
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            }

                            break;
                        case 'c':
                            tiles.set(getGround(), xx, yy);

                            if (Game.RANDOM.nextFloat() < r.difficulty) {
                                try {
                                    Chest c = (Chest) chest.newInstance();

                                    c.x = xx * 8;
                                    c.y = yy * 8;

                                    entities.addEntity(c);
                                } catch (InstantiationException e) {
                                    e.printStackTrace();
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
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
                            try {
                                tiles.set((Tile) ground.newInstance(), xx, yy);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

                            try {
                                Spikes s = (Spikes) spikes.newInstance();

                                s.x = xx * 8;
                                s.y = yy * 8;

                                s.offset = Integer.parseInt("" + template[x][y]) * .1f;

                                entities.addEntity(s);
                            } catch (InstantiationException e) {
                                e.printStackTrace();
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }

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
                    try {
                        tiles.set((Tile) outerWall.newInstance(), r.x1() - 1, y);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!directions.contains(Room.Direction.LEFT, true)) {
                for (int y = r.y0(); y < r.y1(); y++) {
                    try {
                        tiles.set((Tile) outerWall.newInstance(), r.x0(), y);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!directions.contains(Room.Direction.UP, true)) {
                for (int x = r.x0(); x < r.x1(); x++) {
                    try {
                        tiles.set((Tile) outerWall.newInstance(), x, r.y1() - 1);
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (!directions.contains(Room.Direction.DOWN, true)) {
                for (int x = r.x0(); x < r.x1(); x++) {
                    try {
                        tiles.set((Tile) outerWall.newInstance(), x, r.y0());
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // Extend room connections to make sure they are traversable
        for (Room r : rooms) {
            Array<Room.Direction> directions = r.getDirections();

            if (directions.contains(Room.Direction.RIGHT, true)) {
                int x = r.x1() - 1;

                for (int y = r.y0(); y < r.y1(); y++) {
                    if (tiles.at(x, y) != null && isGround(tiles.at(x, y))) {
                        tiles.set(getGround(), x + 1, y);
                    }
                }
            }

            if (directions.contains(Room.Direction.LEFT, true)) {
                int x = r.x0();

                for (int y = r.y0(); y < r.y1(); y++) {
                    if (tiles.at(x, y) != null && isGround(tiles.at(x, y))) {
                        tiles.set(getGround(), x - 1, y);
                    }
                }
            }

            if (directions.contains(Room.Direction.UP, true)) {
                int y = r.y1() - 1;

                for (int x = r.x0(); x < r.x1(); x++) {
                    if (tiles.at(x, y) != null && isGround(tiles.at(x, y))) {
                        tiles.set(getGround(), x, y + 1);
                    }
                }
            }

            if (directions.contains(Room.Direction.DOWN, true)) {
                int y = r.y0();

                for (int x = r.x0(); x < r.x1(); x++) {
                    if (tiles.at(x, y) != null && isGround(tiles.at(x, y))) {
                        tiles.set(getGround(), x, y - 1);
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

        Player player = new Player();
        player.x = rooms.first().getCenterX() * 8 - 4;
        player.y = rooms.first().getCenterY() * 8 - 4;

        this.player = player;
        entities.addEntity(player);

        // Place monsters randomly
        for (Room r : rooms) {
            if (r.spawnMonsters) {
                int monsterCount = (int) MathUtils.lerp(minMonsters, maxMonsters, r.difficulty);

                while (r.getMonsterCount() < monsterCount) {
                    /*Array<String> monsters = new Array<String>();

                    for (Monster m : this.monsters) {
                        if (Game.RANDOM.nextFloat() < MathUtils.lerp(m.minChance, m.maxChance, r.difficulty)) {
                            monsters.add(m.monster);
                        }
                    }*/

                    //if (monsters.size > 0) {
                        //Entity monster = Game.ENTITIES.load(monsters.get(Game.RANDOM.nextInt(monsters.size)));
                        Entity monster = new Bat();

                        int x;
                        int y;

                        do {
                            x = r.getTileX() + 1 + Game.RANDOM.nextInt(r.getTileWidth() - 2);
                            y = r.getTileY() + 1 + Game.RANDOM.nextInt(r.getTileHeight() - 2);
                        } while (tiles.at(x, y) != null && !isGround(tiles.at(x, y)));

                        monster.x = x * 8;
                        monster.y = y * 8;

                        entities.addEntity(monster);
                    //}
                }
            }
        }

        // Lock the doors
        for (Lock l : allLocks) {
            l.onLock();
        }
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

    public Tile getGround() {
        try {
            Class tile = null;

            do {
                for (Ground ground : grounds) {
                    if (Game.RANDOM.nextFloat() < ground.chance) {
                        tile = ground.tile;
                    }
                }
            } while (tile == null);

            return (Tile) tile.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean isGround(Tile t) {
        for (Ground ground : grounds) {
            if (ground.tile.isInstance(t)) {
                return true;
            }
        }

        return false;
    }

    public void shakeScreen(float screenShake) {
        if (screenShake > this.screenShake) {
            this.screenShake = screenShake;
        }
    }


}
