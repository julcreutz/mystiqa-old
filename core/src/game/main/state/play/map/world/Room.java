package game.main.state.play.map.world;

import com.badlogic.gdx.utils.Array;

public class Room {
    public int x;
    public int y;

    public int w;
    public int h;

    public Room parent;
    public Array<Room> children;

    public MonsterComponent monsters;

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

    public int getX0() {
        return x * 8;
    }

    public int getX1() {
        return getX0() + w * 8;
    }

    public int getY0() {
        return y * 4;
    }

    public int getY1() {
        return getY0() + h * 4;
    }

    @Override
    public String toString() {
        return "(" + x + " " + y + " " + w + " " + h + ")";
    }
}
