package game.main.positionable.tile.connected;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import game.resource.SpriteSheet;
import game.main.state.play.map.Map;
import game.main.positionable.tile.Tile;

public class ConnectedTile extends Tile {
    public SpriteSheet spriteSheet;
    public Class[] connectWith;

    public Array<TextureRegion> corners;

    public ConnectedTile() {
        corners = new Array<TextureRegion>();
    }

    @Override
    public void update(Map map) {
        super.update(map);

        int column = 0;
        int row = 0;

        int n = 0;

        boolean r = connectsWith(map, x + 1, y);

        if (r) {
            n++;
        }

        boolean u = connectsWith(map, x, y + 1);

        if (u) {
            n += 2;
        }

        boolean l = connectsWith(map, x - 1, y);

        if (l) {
            n += 4;
        }

        boolean d = connectsWith(map, x, y - 1);

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
        corners.clear();

        boolean rd = connectsWith(map, x + 1, y - 1);
        boolean ld = connectsWith(map, x - 1, y - 1);
        boolean ru = connectsWith(map, x + 1, y + 1);
        boolean lu = connectsWith(map, x - 1, y + 1);

        if (r && d && !rd) {
            corners.add(spriteSheet.grab(4, 0));
        }

        if (l && d && !ld) {
            corners.add(spriteSheet.grab(4, 1));
        }

        if (r && u && !ru) {
            corners.add(spriteSheet.grab(4, 2));
        }

        if (l && u && !lu) {
            corners.add(spriteSheet.grab(4, 3));
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (corners.size > 0) {
            for (TextureRegion corner : corners) {
                batch.draw(corner, getX(), getY());
            }
        }
    }

    public boolean connectsWith(Map map, int x, int y) {
        if (map.tiles.inBounds(x, y)) {
            Tile tile = map.tiles.at(x, y);

            if (tile != null) {
                if (connectWith != null) {
                    for (Class connect : connectWith) {
                        if (connect.isInstance(tile)) {
                            return true;
                        }
                    }
                }

                return tile.getClass().isInstance(this);
            }
        }

        return true;
    }
}
