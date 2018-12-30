package game.main.state.play.map.house;

import com.badlogic.gdx.utils.JsonValue;
import game.main.state.play.map.Map;

public class HouseInterior extends Map {
    @Override
    public void generate() {
        super.generate();

        tiles.initSize(16, 8, 1);
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
        return 1;
    }
}
