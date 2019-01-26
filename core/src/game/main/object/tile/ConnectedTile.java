package game.main.object.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.reference.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.state.play.map.Map;

public class ConnectedTile extends Tile {
    public SpriteSheet spriteSheet;
    public String[] connectWith;

    public TextureRegion corner;

    @Override
    public void update(Map map) {
        super.update(map);

        int column = 0;
        int row = 0;

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
                column = 3;
                row = 3;
                break;
            case 1:
                column = 0;
                row = 3;
                break;
            case 2:
                column = 3;
                row = 2;
                break;
            case 3:
                column = 0;
                row = 2;
                break;
            case 4:
                column = 2;
                row = 3;
                break;
            case 5:
                column = 1;
                row = 3;
                break;
            case 6:
                column = 2;
                row = 2;
                break;
            case 7:
                column = 1;
                row = 2;
                break;
            case 8:
                column = 3;
                row = 0;
                break;
            case 9:
                column = 0;
                row = 0;
                break;
            case 10:
                column = 3;
                row = 1;
                break;
            case 11:
                column = 0;
                row = 1;
                break;
            case 12:
                column = 2;
                row = 0;
                break;
            case 13:
                column = 1;
                row = 0;
                break;
            case 14:
                column = 2;
                row = 1;
                break;
            case 15:
                column = 1;
                row = 1;
                break;
        }

        image = spriteSheet.grab(column, row);

        // Corner cases
        corner = null;

        boolean rd = connectsWith(map, x + 1, y - 1, 0);
        boolean ld = connectsWith(map, x - 1, y - 1, 0);
        boolean ru = connectsWith(map, x + 1, y + 1, 0);
        boolean lu = connectsWith(map, x - 1, y + 1, 0);

        if (r && d && !rd) {
            corner = spriteSheet.grab(4, 0);
        }

        if (l && d && !ld) {
            corner = spriteSheet.grab(4, 1);
        }

        if (r && u && !ru) {
            corner = spriteSheet.grab(4, 2);
        }

        if (l && u && !lu) {
            corner = spriteSheet.grab(4, 3);
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
                        if (_connect.equals(tile.id)) {
                            return true;
                        }
                    }
                }

                return id.equals(tile.id);
            }
        }

        return true;
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue spriteSheet = json.get("spriteSheet");
        if (spriteSheet != null) {
            this.spriteSheet = Game.SPRITE_SHEETS.load(spriteSheet.asString());
        }

        JsonValue connect = json.get("connectWith");
        if (connect != null) {
            this.connectWith = connect.asStringArray();
        }
    }
}
