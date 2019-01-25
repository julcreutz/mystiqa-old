package game.main.object.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.object.GameObject;
import game.main.state.play.map.Map;
import game.main.object.entity.Entity;

public abstract class Tile extends GameObject {
    /**
     * Represents a property of a {@link Tile}. Overlays are rendered when an {@link Entity} walks
     * over the tile this belongs to.
     *
     * This can create interesting effects, e.g. tall grass.
     */
    public class Overlay implements Serializable {
        /** Image to be rendered. */
        public TextureRegion image;

        /**
         * Constructs new instance and directly deserializes it with given json value.
         *
         * @param json json value to deserialize from
         */
        public Overlay(JsonValue json) {
            deserialize(json);
        }

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

        @Override
        public void deserialize(JsonValue json) {
            JsonValue image = json.get("image");
            if (image != null) {
                this.image = Game.SPRITE_SHEETS.load(image.asString()).grab(0, 0);
            }
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

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue solid = json.get("solid");
        if (solid != null) {
            this.solid = solid.asBoolean();
        }

        JsonValue moveSpeed = json.get("moveSpeed");
        if (moveSpeed != null) {
            this.moveSpeed = moveSpeed.asFloat();
        }

        JsonValue forcedDirection = json.get("forcedDirection");
        if (forcedDirection != null) {
            this.forcedDirection = forcedDirection.asInt();
        }

        JsonValue overlay = json.get("overlay");
        if (overlay != null) {
            this.overlay = new Overlay(overlay);
        }
    }
}
