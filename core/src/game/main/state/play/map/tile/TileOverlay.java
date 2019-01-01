package game.main.state.play.map.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.map.entity.Entity;

/**
 * Represents a property of a {@link Tile}. Overlays are rendered when an {@link Entity} walks
 * over the tile this belongs to.
 *
 * This can create interesting effects, e.g. tall grass.
 */
public class TileOverlay implements Serializable {
    /** Image to be rendered. */
    public TextureRegion image;
    /** Colors to be applied to rendered image. */
    public ShaderProgram colors;

    /**
     * Constructs new instance and directly deserializes it with given json value.
     *
     * @param json json value to deserialize from
     */
    public TileOverlay(JsonValue json) {
        deserialize(json);
    }

    /**
     * Renders colored overlay image relative to specified entity. It's positioned
     * relative to the entity's hitbox center.
     *
     * @param batch sprite batch to render to
     * @param e entity to render relative to
     */
    public void render(SpriteBatch batch, Entity e) {
        batch.setShader(colors);
        batch.draw(image, e.hitbox.centerX() - image.getRegionWidth() * .5f, e.hitbox.y() - image.getRegionHeight() * .5f);
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue image = json.get("image");
        if (image != null) {
            this.image = Game.SPRITE_SHEETS.load(image.asString()).sheet[0][0];
        }

        JsonValue colors = json.get("colors");
        if (colors != null) {
            this.colors = Game.PALETTES.load(colors.asStringArray());
        }
    }
}
