package game.main.world_map.tile;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.main.world_map.WorldMap;

public class WorldMapTile {
    public WorldMapTileType type;

    public TextureRegion image;

    public int x;
    public int y;

    public WorldMapTile(WorldMapTileType type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    public void update(WorldMap map) {
        int n = 0;

        if (connect(map, x + 1, y)) {
            n++;
        }

        if (connect(map, x, y + 1)) {
            n += 2;
        }

        if (connect(map, x - 1, y)) {
            n += 4;
        }

        if (connect(map, x, y - 1)) {
            n += 8;
        }

        switch (n) {
            case 0:
                image = type.sheet[3][3];
                break;
            case 1:
                image = type.sheet[0][3];
                break;
            case 2:
                image = type.sheet[3][2];
                break;
            case 3:
                image = type.sheet[0][2];
                break;
            case 4:
                image = type.sheet[2][3];
                break;
            case 5:
                image = type.sheet[1][3];
                break;
            case 6:
                image = type.sheet[2][2];
                break;
            case 7:
                image = type.sheet[1][2];
                break;
            case 8:
                image = type.sheet[3][0];
                break;
            case 9:
                image = type.sheet[0][0];
                break;
            case 10:
                image = type.sheet[3][1];
                break;
            case 11:
                image = type.sheet[0][1];
                break;
            case 12:
                image = type.sheet[2][0];
                break;
            case 13:
                image = type.sheet[1][0];
                break;
            case 14:
                image = type.sheet[2][1];
                break;
            case 15:
                image = type.sheet[1][1];
                break;
        }
    }

    public void render(SpriteBatch batch) {
        batch.setColor(type.color);
        batch.draw(image, x * 8, y * 8);
        batch.setColor(1, 1, 1, 1);
        batch.setShader(null);
    }

    public boolean connect(WorldMap map, int x, int y) {
        return x >= 0 && x < map.tiles.length && y >= 0 && y < map.tiles[0].length && map.tiles[x][y] != null && type.connect(map.tiles[x][y].type);
    }
}
