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

    public TextureRegion[][] spriteSheet;

    public ShaderProgram[] palettes;
    public float paletteSpeed;

    public boolean autoTile;

    public String[] connect;

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
                    image = spriteSheet[3][3];
                    break;
                case 1:
                    image = spriteSheet[0][3];
                    break;
                case 2:
                    image = spriteSheet[3][2];
                    break;
                case 3:
                    image = spriteSheet[0][2];
                    break;
                case 4:
                    image = spriteSheet[2][3];
                    break;
                case 5:
                    image = spriteSheet[1][3];
                    break;
                case 6:
                    image = spriteSheet[2][2];
                    break;
                case 7:
                    image = spriteSheet[1][2];
                    break;
                case 8:
                    image = spriteSheet[3][0];
                    break;
                case 9:
                    image = spriteSheet[0][0];
                    break;
                case 10:
                    image = spriteSheet[3][1];
                    break;
                case 11:
                    image = spriteSheet[0][1];
                    break;
                case 12:
                    image = spriteSheet[2][0];
                    break;
                case 13:
                    image = spriteSheet[1][0];
                    break;
                case 14:
                    image = spriteSheet[2][1];
                    break;
                case 15:
                    image = spriteSheet[1][1];
                    break;
            }
        } else {
            image = spriteSheet[0][0];
        }
    }

    public void render(SpriteBatch batch) {
        batch.setShader(palettes[(int) ((Game.time * paletteSpeed) % palettes.length)]);
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
        JsonValue name = json.get("name");
        if (name != null) {
            this.name = name.asString();
        }

        JsonValue spriteSheet = json.get("spriteSheet");
        if (spriteSheet != null) {
            this.spriteSheet = Game.SPRITE_SHEETS.load(spriteSheet.asString()).sheet;
        }

        JsonValue palettes = json.get("palettes");
        if (palettes != null) {
            this.palettes = new ShaderProgram[palettes.size];

            for (int i = 0; i < palettes.size; i++) {
                this.palettes[i] = Game.PALETTES.load(palettes.get(i).asStringArray());
            }
        }

        JsonValue paletteSpeed = json.get("paletteSpeed");
        if (paletteSpeed != null) {
            this.paletteSpeed = paletteSpeed.asFloat();
        }

        JsonValue autoTile = json.get("autoTile");
        if (autoTile != null) {
            this.autoTile = autoTile.asBoolean();
        }

        JsonValue connect = json.get("connect");
        if (connect != null) {
            this.connect = connect.asStringArray();
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
