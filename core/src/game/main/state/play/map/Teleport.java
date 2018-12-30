package game.main.state.play.map;

import com.badlogic.gdx.math.Rectangle;

public class Teleport {
    public Rectangle rect;

    public Map destination;

    public float destinationX;
    public float destinationY;

    public Teleport(float x, float y, float width, float height) {
        rect = new Rectangle(x, y, width, height);
    }
}
