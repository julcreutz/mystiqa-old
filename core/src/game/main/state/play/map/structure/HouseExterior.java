package game.main.state.play.map.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.range.IntRange;
import game.main.Game;
import game.main.state.play.map.house.HouseInterior;
import game.main.state.play.map.tile.Tile;
import game.main.state.play.map.world.World;

import java.util.Random;

public class HouseExterior extends Structure<World> {
    public String entrance;
    public String wall;
    public String roof;

    public IntRange width;
    public IntRange height;

    public int w;
    public int h;

    public String interior;

    @Override
    public void generate(Random rand, World map, int x, int y, int z) {
        for (int xx = -1; xx < w + 1; xx++) {
            for (int yy = -1; yy < h + 1; yy++) {
                Tile t = map.tiles.at(x + xx, y + yy, 0);

                if (t != null && t.name.equals(map.biomes[(x + xx) / 16][(y + yy) / 8].wall.tile())) {
                    map.tiles.clear(x + xx, y + yy);
                    map.tiles.set(Game.TILES.load(map.biomes[x / 16][y / 8].ground), x + xx, y + yy, 0);
                }
            }
        }

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                map.tiles.clear(x + xx, y + yy);
                map.tiles.set(Game.TILES.load(wall), x + xx, y + yy, 0);
                map.tiles.set(Game.TILES.load(roof), x + xx, y + yy, 1);
            }
        }

        int ex = w / 2;
        int ey = 0;

        map.tiles.set(Game.TILES.load(entrance), x + ex, y + ey, 0);

        map.tiles.clear(x + ex, y + ey - 1);
        map.tiles.set(Game.TILES.load(map.biomes[(x + ex) / 16][(y + ey - 1) / 8].ground), x + ex, y + ey - 1, 0);

        HouseInterior house = (HouseInterior) Game.MAPS.load(this.interior);
        map.connect(house, (x + ex) * 8, (y + ey) * 8, house.getEntranceX() * 8, house.getEntranceY() * 8);
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("entrance")) {
            entrance = json.getString("entrance");
        }

        if (json.has("wall")) {
            wall = json.getString("wall");
        }

        if (json.has("roof")) {
            roof = json.getString("roof");
        }

        if (json.has("width")) {
            width = new IntRange();
            width.deserialize(json.get("width"));
        }

        if (json.has("height")) {
            height = new IntRange();
            height.deserialize(json.get("height"));
        }

        if (json.has("interior")) {
            interior = json.getString("interior");
        }
    }
}
