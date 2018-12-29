package game.main.state.play.entity;

import com.badlogic.gdx.math.Rectangle;

/**
 * Describes a rectangle offset by specified x and y values relative to
 * a given entity. Uses {@link Rectangle} class to represent the rectangle itself.
 *
 * @see Rectangle
 */
public class Hitbox {
    /** The rectangle itself. */
    public Rectangle rect;

    /** The value the rectangle is offset by on x axis relative to an entity. */
    public float offsetX;

    /** The value of the rectangle is offset by on y axis relative to an entity. */
    public float offsetY;

    /** Constructs the hitbox initializing it's rectangle to prevent
     *  potential {@link java.lang.NullPointerException}.
     */
    public Hitbox() {
        rect = new Rectangle();
    }

    /**
     * Positions rectangle relative to specified entity isUsing it's offsets and two additional offset values.
     *
     * @param e Entity to position relative to
     * @param offsetX additional x offset
     * @param offsetY additional y offset
     */
    public void position(Entity e, float offsetX, float offsetY) {
        rect.setPosition(e.x + this.offsetX + offsetX, e.y + this.offsetY + offsetY);
    }

    /**
     * Positions rectangle relative to specified entity isUsing it's offsets.
     *
     * @param e Entity to position relative to
     */
    public void position(Entity e) {
        position(e, 0, 0);
    }

    /**
     * Checks whether the specified rectangle overlaps.
     * Only works if the both rectangle's width and height are greater than zero.
     *
     * @param rect rectangle to check overlap
     * @return whether the rectangle overlaps
     */
    public boolean overlaps(Rectangle rect) {
        return getWidth() > 0 && getHeight() > 0 && rect.width > 0 && rect.height > 0 && this.rect.overlaps(rect);
    }

    /**
     * Convenience method using to check whether the specified hitbox overlaps.
     * {@link #overlaps(Rectangle)} for more details.
     *
     * @param hitbox hitbox to check overlap
     * @return whether the hitbox overlaps
     */
    public boolean overlaps(Hitbox hitbox) {
        return overlaps(hitbox.rect);
    }

    /**
     * Convenience method using to check whether the specified entity,
     * more specifically it's hitbox, overlaps.
     * {@link #overlaps(Rectangle)} for more details.
     *
     * @param e entity to check overlap
     * @return whether the hitbox overlaps
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
