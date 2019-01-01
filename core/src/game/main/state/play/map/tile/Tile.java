package game.main.state.play.map.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.map.Map;

public class Tile implements Serializable {
    public String name;

    public TextureRegion[][] sheet;

    public Array<ShaderProgram> palettes;
    public float paletteSpeed;

    public boolean autoTile;

    public String[] connect;

    public boolean solid;

    public float moveSpeed;

    public int forcedDirection;

    public Overlay overlay;

    public TextureRegion image;

    public int x;
    public int y;
    public int z;

    public boolean updated;

    public void update(Map map) {
        if (autoTile) {
            int n = 0;

            if (connectsTo(map, x + 1, y, z)) {
                n++;
            }

            if (connectsTo(map, x, y + 1, z)) {
                n += 2;
            }

            if (connectsTo(map, x - 1, y, z)) {
                n += 4;
            }

            if (connectsTo(map, x, y - 1, z)) {
                n += 8;
            }

            switch (n) {
                case 0:
                    image = sheet[3][3];
                    break;
                case 1:
                    image = sheet[0][3];
                    break;
                case 2:
                    image = sheet[3][2];
                    break;
                case 3:
                    image = sheet[0][2];
                    break;
                case 4:
                    image = sheet[2][3];
                    break;
                case 5:
                    image = sheet[1][3];
                    break;
                case 6:
                    image = sheet[2][2];
                    break;
                case 7:
                    image = sheet[1][2];
                    break;
                case 8:
                    image = sheet[3][0];
                    break;
                case 9:
                    image = sheet[0][0];
                    break;
                case 10:
                    image = sheet[3][1];
                    break;
                case 11:
                    image = sheet[0][1];
                    break;
                case 12:
                    image = sheet[2][0];
                    break;
                case 13:
                    image = sheet[1][0];
                    break;
                case 14:
                    image = sheet[2][1];
                    break;
                case 15:
                    image = sheet[1][1];
                    break;
            }
        } else {
            image = sheet[0][0];
        }
    }

    public void render(SpriteBatch batch) {
        batch.setShader(palettes.get((int) ((Game.time * paletteSpeed) % palettes.size)));
        batch.draw(image, x * 8, y * 8 + z * 8);
        batch.setColor(1, 1, 1, 1);
        batch.setShader(null);
    }

    public boolean connectsTo(Map map, int x, int y, int z) {
        if (map.tiles.inBounds(x, y, z)) {
            Tile tile = map.tiles.tileAt(x, y, z);

            if (tile != null) {
                if (connect != null) {
                    for (String _connect : connect) {
                        if (_connect.equals(tile.name)) {
                            return true;
                        }
                    }
                }

                return name.equals(tile.name);
            }
        }

        return false;
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

        paletteSpeed = json.getFloat("paletteSpeed", 0);

        autoTile = json.getBoolean("autoTile", false);

        if (json.has("connect")) {
            connect = json.get("connect").asStringArray();
        }

        solid = json.getBoolean("solid", false);

        moveSpeed = json.getFloat("moveSpeed", 1);

        forcedDirection = json.getInt("forcedDirection", -1);

        JsonValue overlay = json.get("overlay");
        if (overlay != null) {
            this.overlay = new Overlay(overlay);
        }
    }
}
