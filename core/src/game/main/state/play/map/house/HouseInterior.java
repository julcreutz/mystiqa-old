package game.main.state.play.map.house;

import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;
import game.main.state.play.map.Map;

public class HouseInterior extends Map {
    @Override
    public void generate() {
        super.generate();

        tiles.initSize(16, 8, 1);

        for (int x = 0; x < tiles.getWidth(); x++) {
            for (int y = 0; y < tiles.getHeight(); y++) {
                if (x == 0 || x == tiles.getWidth() - 1 || y == 0 || y == tiles.getHeight() - 1) {
                    tiles.set(Game.TILES.load("Stone"), x, y, 0);
                } else {
                    tiles.set(Game.TILES.load("Dirt"), x, y, 0);
                }
            }
        }

        tiles.set(Game.TILES.load("Dirt"), getEntranceX(), getEntranceY(), 0);
    }

    @Override
    public void placePlayer() {

    }

    @Override
    public void deserialize(JsonValue json) {

    }

    public int getEntranceX() {
        return 8;
    }

    public int getEntranceY() {
        return 0;
    }
}
