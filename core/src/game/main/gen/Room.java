package game.main.gen;

import com.badlogic.gdx.utils.Array;

public class Room {
    public int x;
    public int y;

    public int w;
    public int h;

    public Room parent;
    public Array<Room> children;

    public Room(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;

        children = new Array<>();
    }

    public Room() {
        this(0, 0, 0, 0);
    }
}
