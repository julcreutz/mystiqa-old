package engine.entity;

/**
 * Base interface for entities.
 * Extend this class to classify different types
 * of entities, e.g. actors, tiles, etc.
 */
public interface Entity {
    /**
     * @return x coordinate
     */
    float getX();

    /**
     * @return y coordinate
     */
    float getY();

    /**
     * @param x new x coordinate
     */
    void setX(float x);

    /**
     * @param y new y coordinate
     */
    void setY(float y);
}
