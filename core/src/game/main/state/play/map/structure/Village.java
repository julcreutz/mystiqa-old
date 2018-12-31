package game.main.state.play.map.structure;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.Range;
import game.main.Game;
import game.main.state.play.map.Map;
import game.main.state.play.map.Node;
import game.main.state.play.map.world.Biome;
import game.main.state.play.map.world.Room;
import game.main.state.play.map.world.World;

import java.awt.*;
import java.util.Random;

public class Village extends Structure<World> {
    public String house;
    public Range count;

    @Override
    public void generate(Random rand, World map, int x, int y, int z) {
        Array<Rectangle> houses = new Array<Rectangle>();

        for (int i = 0; i < count.pickRandom(rand); i++) {
            Rectangle r0 = new Rectangle();

            while (true) {
                Room r = map.roomAt(x / 16 + rand.nextInt(4), y / 8 + rand.nextInt(4));

                r0.width = ((HouseExterior) Game.STRUCTURES.load(house)).width.pickRandom(rand);
                r0.height = ((HouseExterior) Game.STRUCTURES.load(house)).height.pickRandom(rand);

                final int dist = 2;

                r0.x = r.x0() + dist + rand.nextInt(MathUtils.clamp(r.w * 8 - dist * 2 - r0.width, 1, Integer.MAX_VALUE));
                r0.y = r.y0() + dist + rand.nextInt(MathUtils.clamp(r.h * 4 - dist * 2 - r0.height, 1, Integer.MAX_VALUE));

                boolean overlaps = false;

                for (Rectangle r1 : houses) {
                    if (r0.intersects(new Rectangle(r1.x - 1, r1.y - 1, r1.width + 2, r1.height + 2))) {
                        overlaps = true;
                    }
                }

                if (!overlaps) {
                    houses.add(r0);
                    break;
                }
            }
        }

        for (Rectangle r : houses) {
            HouseExterior house = (HouseExterior) Game.STRUCTURES.load(this.house);

            house.w = r.width;
            house.h = r.height;

            house.generate(rand, map, r.x, r.y, 0);
        }

        for (int i = 0; i < houses.size - 1; i++) {
            Rectangle r0 = houses.get(i);
            Rectangle r1 = houses.get(i + 1);

            Array<Node> path = map.findPath(r0.x + r0.width / 2, r0.y - 1, r1.x + r1.width / 2, r1.y - 1);

            if (path != null) {
                for (Node node : path) {
                    if (map.tiles.tileAt(node.x, node.y, 0) != null && map.tiles.tileAt(node.x, node.y, 0).type == map.biomes[node.x / 16][node.y / 8].ground) {
                        map.tiles.placeTile(Game.TILES.load("Path"), node.x, node.y, 0);
                    }
                }
            }
        }
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("house")) {
            house = json.getString("house");
        }

        if (json.has("count")) {
            count = new Range();
            count.deserialize(json.get("count"));
        }
    }
}
