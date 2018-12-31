package game.main.state.play.map.structure;

import com.badlogic.gdx.utils.JsonValue;
import game.Range;
import game.main.Game;
import game.main.state.play.map.Map;
import game.main.state.play.map.house.HouseInterior;
import game.main.state.play.map.tile.Tile;
import game.main.state.play.map.world.World;

import java.util.Random;

public class HouseExterior extends Structure<World> {
    public Tile.Type entrance;
    public Tile.Type wall;
    public Tile.Type roof;

    public Range width;
    public Range height;

    public int w;
    public int h;

    public String interior;

    @Override
    public void generate(Random rand, World map, int x, int y, int z) {
        for (int xx = -1; xx < w + 1; xx++) {
            for (int yy = -1; yy < h + 1; yy++) {
                Tile t = map.tiles.tileAt(x + xx, y + yy, 0);

                if (t != null && t.type == map.biomes[(x + xx) / 16][(y + yy) / 8].wall.tile()) {
                    map.tiles.erase(x + xx, y + yy);
                    map.tiles.placeTile(map.biomes[x / 16][y / 8].ground, x + xx, y + yy, 0);
                }
            }
        }

        for (int xx = 0; xx < w; xx++) {
            for (int yy = 0; yy < h; yy++) {
                map.tiles.erase(x + xx, y + yy);
                map.tiles.placeTile(wall, x + xx, y + yy, 0);
                map.tiles.placeTile(roof, x + xx, y + yy, 1);
            }
        }

        int ex = w / 2;
        int ey = 0;

        map.tiles.placeTile(entrance, x + ex, y + ey, 0);

        map.tiles.erase(x + ex, y + ey - 1);
        map.tiles.placeTile(map.biomes[(x + ex) / 16][(y + ey - 1) / 8].ground, x + ex, y + ey - 1, 0);

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
