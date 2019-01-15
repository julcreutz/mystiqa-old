package game.loader.instance;

import game.main.state.play.map.Map;
import game.main.state.play.map.dungeon.Dungeon;

public class MapLoader extends InstanceLoader<Map> {
    public MapLoader(String root) {
        super(root);
    }

    @Override
    public Map newInstance(String name) {
        if (name.equals("Dungeon")) {
            return new Dungeon();
        }

        return null;
    }
}
