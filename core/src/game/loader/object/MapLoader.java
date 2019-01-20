package game.loader.object;

import game.main.state.play.map.Map;
import game.main.state.play.map.dungeon.Dungeon;

public class MapLoader extends ObjectLoader<Map> {
    @Override
    public Map newInstance(String name) {
        if (name.equals("Dungeon")) {
            return new Dungeon();
        }

        return null;
    }
}
