package game.main.world_map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WorldMapTile {
    public WorldMapTileType type;

    public boolean drawLeft;
    public boolean drawRight;
    public boolean drawDown;
    public boolean drawUp;

    public int x;
    public int y;

    public void update(WorldMap map) {
        drawLeft = !sameType(map, x - 1, y);
        drawRight = !sameType(map, x + 1, y);
        drawDown = !sameType(map, x, y - 1);
        drawUp = !sameType(map, x, y + 1);
    }

    public void render(SpriteBatch batch) {
        batch.draw(type.sheet[1][1], x * 8, y * 8);

        if (drawLeft) {
            batch.draw(type.sheet[0][1], x * 8 - 8, y * 8);
        }

        if (drawRight) {
            batch.draw(type.sheet[2][1], x * 8 + 8, y * 8);
        }

        if (drawDown) {
            batch.draw(type.sheet[1][2], x * 8, y * 8 - 8);
        }

        if (drawUp) {
            batch.draw(type.sheet[1][0], x * 8, y * 8 + 8);
        }
    }

    public boolean sameType(WorldMap map, int x, int y) {
        return x >= 0 && x < map.tiles.length && y >= 0 && y < map.tiles[0].length && map.tiles[x][y] != null && map.tiles[x][y].type.name.equals(type.name);
    }
}
