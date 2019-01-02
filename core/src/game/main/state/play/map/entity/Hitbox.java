package game.main.state.play.map.entity;

import com.badlogic.gdx.math.Rectangle;

/**
 * Describes a rectangle offset by specified x and y values relative to
 * a given entity. Uses {@link Rectangle} class to represent the rectangle itself.
 *
 * @see Rectangle
 */
public class Hitbox {
    /** Holds reference of entity this applies to. */
    public Entity e;

    /** Rectangle itself. */
    public Rectangle rect;

    /** Value the rectangle is offset by on x axis relative to an entity. */
    public float offsetX;

    /** Value of the rectangle is offset by on y axis relative to an entity. */
    public float offsetY;

    /**
     * Constructs hitbox initializing rectangle to prevent
     * potential {@link java.lang.NullPointerException}.
     */
    public Hitbox(Entity e) {
        this.e = e;
        rect = new Rectangle();
    }

    /**
     * Positions rectangle relative to entity using offsets and two additional offset values.
     *
     * @param offsetX additional x offset
     * @param offsetY additional y offset
     */
    public void position(float offsetX, float offsetY) {
        rect.setPosition(e.x + this.offsetX + offsetX, e.y + this.offsetY + offsetY);
    }

    /** Positions rectangle relative to entity using offsets. */
    public void position() {
        position(0, 0);
    }

    /**
     * Checks whether specified rectangle overlaps.
     * Only works if both rectangle's width and height are greater than zero.
     *
     * @param rect rectangle to check overlap
     * @return whether rectangle overlaps
     */
    public boolean overlaps(Rectangle rect) {
        return getWidth() > 0 && getHeight() > 0 && rect.width > 0 && rect.height > 0 && this.rect.overlaps(rect);
    }

    /**
     * Convenience method to check whether specified hitbox overlaps.
     * See {@link #overlaps(Rectangle)} for more details.
     *
     * @param hitbox hitbox to check overlap
     * @return whether hitbox overlaps
     */
    public boolean overlaps(Hitbox hitbox) {
        return overlaps(hitbox.rect);
    }

    /**
     * Convenience method to check whether specified entity's hitbox overlaps.
     * See {@link #overlaps(Rectangle)} for more details.
     *
     * @param e entity to check overlap
     * @return whether hitbox overlaps
     */
    public boolean overlaps(Entity e) {
        return overlaps(e.hitbox);
    }

    /** @return x position of rectangle */
    public float getX() {
        return rect.x;
    }

    /** @return y position of rectangle */
    public float getY() {
        return rect.y;
    }

    /** @return width of rectangle */
    public float getWidth() {
        return rect.width;
    }

    /** @return height of rectangle */
    public float getHeight() {
        return rect.height;
    }

    /** @return x center position of rectangle */
    public float getCenterX() {
        return getX() + getWidth() * .5f;
    }

    /** @return y center position of rectangle */
    public float getCenterY() {
        return getY() + getHeight() * .5f;
    }

    /**
     * Convenience method to initialize all important values at once.
     *
     * @param width width of rectangle
     * @param height height of rectangle
     * @param offsetX x offset of rectangle relative to entity
     * @param offsetY y offset of rectangle relative to entity
     */
    public void set(float width, float height, float offsetX, float offsetY) {
        rect.setSize(width, height);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }
}
