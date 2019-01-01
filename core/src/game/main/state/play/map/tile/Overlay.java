package game.main.state.play.map.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.map.entity.Entity;

public class Overlay implements Serializable {
    public TextureRegion image;
    public ShaderProgram colors;

    public Overlay(JsonValue json) {
        deserialize(json);
    }

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
