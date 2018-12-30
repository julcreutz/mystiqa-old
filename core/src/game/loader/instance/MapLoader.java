package game.loader.instance;

import game.main.state.play.map.Map;
import game.main.state.play.map.house.HouseInterior;
import game.main.state.play.map.world.World;

public class MapLoader extends InstanceLoader<Map> {
    @Override
    public Map newInstance(String name) {
        if (name.equals("World")) {
            return new World();
        } else if (name.equals("HouseInterior")) {
            return new HouseInterior();
        }

        return null;
    }
}