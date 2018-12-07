package game.entity;

import engine.entity.Entity;

/**
 * Actors are a type of entity that move and
 * react to input. In comparison to tiles,
 * they are not static.
 */
public class Actor implements Entity {
    /**
     * Current x coordinate.
     */
    private float x;

    /**
     * Current y coordinate.
     */
    private float y;

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public void setX(float x) {
        this.x = x;
    }

    @Override
    public void setY(float y) {
        this.y = y;
    }
}
