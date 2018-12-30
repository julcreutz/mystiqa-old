package game.main.state.play.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;

public class TileType implements Serializable {
    public String name;

    public TextureRegion[][] sheet;

    public Array<ShaderProgram> palettes;
    public float paletteSpeed;

    public boolean autoTile;

    public String[] connect;

    public boolean solid;

    public float moveSpeed;

    public int forcedDirection;

    public boolean connectsTo(TileType type) {
        if (connect != null) {
            for (String _connect : connect) {
                if (_connect.equals(type.name)) {
                    return true;
                }
            }
        }

        return name.equals(type.name);
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("name")) {
            name = json.getString("name");
        }

        if (json.has("sheet")) {
            sheet = Game.SPRITE_SHEETS.load(json.getString("sheet")).sheet;
        }

        if (json.has("palettes")) {
            palettes = new Array<ShaderProgram>();

            for (JsonValue palette : json.get("palettes")) {
                palettes.add(Game.PALETTES.load(palette.asStringArray()));
            }
        }

        if (json.has("paletteSpeed")) {
            paletteSpeed = json.getFloat("paletteSpeed");
        }

        if (json.has("autoTile")) {
            autoTile = json.getBoolean("autoTile");
        }

        if (json.has("connect")) {
            connect = json.get("connect").asStringArray();
        }

        if (json.has("solid")) {
            solid = json.getBoolean("solid");
        }

        moveSpeed = json.getFloat("moveSpeed", 1);

        forcedDirection = json.getInt("forcedDirection", -1);
    }
}
