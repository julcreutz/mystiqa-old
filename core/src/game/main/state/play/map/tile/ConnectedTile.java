package game.main.state.play.map.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;
import game.main.state.play.map.Map;

public class ConnectedTile extends Tile {
    public TextureRegion[][] spriteSheet;
    public String[] connectWith;

    public TextureRegion corner;

    @Override
    public void update(Map map) {
        super.update(map);

        int n = 0;

        boolean r = connectsWith(map, x + 1, y, z);

        if (r) {
            n++;
        }

        boolean u = connectsWith(map, x, y + 1, z);

        if (u) {
            n += 2;
        }

        boolean l = connectsWith(map, x - 1, y, z);

        if (l) {
            n += 4;
        }

        boolean d = connectsWith(map, x, y - 1, z);

        if (d) {
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

        // Corner cases
        corner = null;

        boolean rd = connectsWith(map, x + 1, y - 1, 0);
        boolean ld = connectsWith(map, x - 1, y - 1, 0);
        boolean ru = connectsWith(map, x + 1, y + 1, 0);
        boolean lu = connectsWith(map, x - 1, y + 1, 0);

        if (r && d && !rd) {
            corner = spriteSheet[4][0];
        }

        if (l && d && !ld) {
            corner = spriteSheet[4][1];
        }

        if (r && u && !ru) {
            corner = spriteSheet[4][2];
        }

        if (l && u && !lu) {
            corner = spriteSheet[4][3];
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (corner != null) {
            batch.draw(corner, x * 8, y * 8 + z * 8);
        }
    }

    public boolean connectsWith(Map map, int x, int y, int z) {
        if (map.tiles.inBounds(x, y, z)) {
            Tile tile = map.tiles.at(x, y, z);

            if (tile != null) {
                if (connectWith != null) {
                    for (String _connect : connectWith) {
                        if (_connect.equals(tile.name)) {
                            return true;
                        }
                    }
                }

                return name.equals(tile.name);
            }
        }

        return true;
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue spriteSheet = json.get("spriteSheet");
        if (spriteSheet != null) {
            this.spriteSheet = Game.SPRITE_SHEETS.load(spriteSheet.asString()).sheet;
        }

        JsonValue connect = json.get("connectWith");
        if (connect != null) {
            this.connectWith = connect.asStringArray();
        }
    }
}
