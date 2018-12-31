package game.main.state.play.map.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.Range;
import game.main.Game;
import game.main.state.play.map.Map;
import game.main.state.play.map.house.HouseInterior;
import game.main.state.play.map.tile.Tile;

import java.util.Random;

public class HouseExterior extends Structure {
    public Tile.Type entrance;
    public Tile.Type wall;
    public Tile.Type roof;

    public Range width;
    public Range height;

    public String interior;

    @Override
    public void generate(Random rand, Map map, int x, int y, int z) {
        int width = this.width.pickRandom(rand);
        int height = this.height.pickRandom(rand);

        for (int xx = 0; xx < width; xx++) {
            for (int yy = 0; yy < height; yy++) {
                map.tiles.erase(x + xx, y + yy);
                map.tiles.placeTile(wall, x + xx, y + yy, 0);
                map.tiles.placeTile(roof, x + xx, y + yy, 1);
            }
        }

        int ex = width / 2;
        int ey = 0;

        map.tiles.placeTile(entrance, x + ex, y + ey, 0);

        HouseInterior house = (HouseInterior) Game.MAPS.load(this.interior);
        map.connect(house, (x + ex) * 8, (y + ey) * 8, house.getEntranceX() * 8, house.getEntranceY() * 8);
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("entrance")) {
            entrance = Game.TILES.load(json.getString("entrance"));
        }

        if (json.has("wall")) {
            wall = Game.TILES.load(json.getString("wall"));
        }

        if (json.has("roof")) {
            roof = Game.TILES.load(json.getString("roof"));
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
