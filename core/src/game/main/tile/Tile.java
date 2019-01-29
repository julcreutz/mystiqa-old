package game.main.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import game.main.state.play.map.Map;
import game.main.entity.Entity;

public abstract class Tile {
    /**
     * Represents a property of a {@link Tile}. Overlays are rendered when an {@link Entity} walks
     * over the tile this belongs to.
     *
     * This can create interesting effects, e.g. tall grass.
     */
    public class Overlay {
        /** Image to be rendered. */
        public TextureRegion image;

        /**
         * Renders colored overlay image multiplier to specified entity. It's positioned
         * multiplier to the entity's hitbox center.
         *
         * @param batch sprite batch to render to
         * @param e entity to render multiplier to
         */
        public void render(SpriteBatch batch, Entity e) {
            batch.draw(image, e.hitbox.getCenterX() - image.getRegionWidth() * .5f, e.hitbox.getY() - image.getRegionHeight() * .5f);
        }
    }

    public boolean solid;

    public float moveSpeed;

    public int forcedDirection;

    public Overlay overlay;

    public TextureRegion image;

    public int x;
    public int y;
    public int z;

    public boolean updated;

    public Tile() {
        moveSpeed = 1;
        forcedDirection = -1;
    }

    public void update(Map map) {

    }

    public void render(SpriteBatch batch) {
        batch.draw(image, x * 8, y * 8 + z * 8);
    }
}
