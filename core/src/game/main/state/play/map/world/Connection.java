package game.main.state.play.map.world;

import com.badlogic.gdx.utils.Array;

import java.awt.*;

public class Connection {
    public Array<Point> points;

    public int absoluteDiffX;
    public int absoluteDiffY;

    public int wayThickness;

    public Connection() {
        points = new Array<Point>();
    }
}
