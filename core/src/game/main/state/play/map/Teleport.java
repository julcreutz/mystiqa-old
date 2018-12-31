package game.main.state.play.map;

import com.badlogic.gdx.math.Rectangle;

public class Teleport {
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
