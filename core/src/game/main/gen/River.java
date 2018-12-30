package game.main.gen;

import com.badlogic.gdx.utils.Array;

import java.awt.*;

public class River {
    public Array<Point> points;
    public int thickness;

    public River(int thickness) {
        points = new Array<Point>();
        this.thickness = thickness;
    }
}
