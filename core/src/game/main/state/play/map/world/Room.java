package game.main.state.play.map.world;

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

        children = new Array<Room>();
    }

    public Room() {
        this(0, 0, 0, 0);
    }

    public int x0() {
        return x * 8;
    }

    public int x1() {
        return x0() + w * 8;
    }

    public int y0() {
        return y * 4;
    }

    public int y1() {
        return y0() + h * 4;
    }

    @Override
    public String toString() {
        return "(" + x + " " + y + " " + w + " " + h + ")";
    }
}
