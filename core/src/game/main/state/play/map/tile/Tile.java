package game.main.state.play.map.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.map.Map;

public abstract class Tile implements Serializable {
    public String name;

    public ShaderProgram[] colors;
    public float colorSwitchSpeed;

    public boolean solid;

    public float moveSpeed;

    public int forcedDirection;

    public TileOverlay overlay;

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
        batch.setShader(colors[(int) ((Game.time * colorSwitchSpeed) % colors.length)]);
        batch.draw(image, x * 8, y * 8 + z * 8);
        batch.setColor(1, 1, 1, 1);
        batch.setShader(null);
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue name = json.get("name");
        if (name != null) {
            this.name = name.asString();
        }

        JsonValue palettes = json.get("colors");
        if (palettes != null) {
            this.colors = new ShaderProgram[palettes.size];

            for (int i = 0; i < palettes.size; i++) {
                this.colors[i] = Game.PALETTES.load(palettes.get(i).asStringArray());
            }
        }

        JsonValue paletteSpeed = json.get("colorSwitchSpeed");
        if (paletteSpeed != null) {
            this.colorSwitchSpeed = paletteSpeed.asFloat();
        }

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
            this.overlay = new TileOverlay(overlay);
        }
    }
}
