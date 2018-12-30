package game.loader.instance;

import game.main.state.play.map.structure.SingleTile;
import game.main.state.play.map.structure.Structure;
import game.main.state.play.map.structure.Tree;

public class StructureLoader extends InstanceLoader<Structure> {
    @Override
    public Structure newInstance(String name) {
        Structure s = null;

        if (name.equals("Tree")) {
            s = new Tree();
        } else if (name.equals("SingleTile")) {
            s = new SingleTile();
        }

        return s;
    }
}
