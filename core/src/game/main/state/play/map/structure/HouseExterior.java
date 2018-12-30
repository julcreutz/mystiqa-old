package game.main.state.play.map.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.Range;
import game.main.Game;
import game.main.state.play.map.Map;
import game.main.state.play.map.house.HouseInterior;
import game.main.state.play.map.tile.TileType;

import java.util.Random;

public class HouseExterior extends Structure {
    public TileType wall;

    public Range width;
    public Range height;

    public String interior;

    @Override
    public void generate(Random rand, Map map, int x, int y, int z) {
        int width = this.width.pickRandom(rand);
        int height = this.height.pickRandom(rand);

        int ex = width / 2;
        int ey = 0;

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                if (xx != ex || yy != ey) {
                    map.tiles.placeTile(wall, x + xx, y + yy, 0);
                }
            }
        }

        HouseInterior house = (HouseInterior) Game.MAPS.load(this.interior);
        map.connect(house, (x + ex) * 8, (y + ey) * 8, house.getEntranceX() * 8, house.getEntranceY() * 8);
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("wall")) {
            wall = Game.TILES.load(json.getString("wall"));
        }

        if (json.has("width")) {
            width = new Range();
            width.deserialize(json.get("width"));
        }

        if (json.has("height")) {
            height = new Range();
            height.deserialize(json.get("height"));
        }

        if (json.has("interior")) {
            interior = json.getString("interior");
        }
    }
}
