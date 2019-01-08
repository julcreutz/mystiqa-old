package game.main.state.play.map.dungeon;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;
import game.main.state.play.map.Map;
import game.main.state.play.map.entity.Humanoid;
import game.main.state.play.map.tile.TileManager;

public class Dungeon extends Map {
    public static class Room {
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
    }

    public static final int WIDTH = 16;
    public static final int HEIGHT = 8;

    public static final int ROOMS = 10;

    public Array<Room> rooms;

    @Override
    public void generate() {
        super.generate();

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

        for (Room r : rooms) {
            for (int x = (int) (r.rect.x * 16); x < r.rect.x * 16 + r.rect.width * 16; x++) {
                for (int y = (int) (r.rect.y * 8); y < r.rect.y * 8 + r.rect.height * 8; y++) {
                    tiles.set(Game.TILES.load("Grass"), x, y, 0);
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

    }
}
