package game.loader.instance;

import game.main.state.play.structure.SingleTile;
import game.main.state.play.structure.Structure;
import game.main.state.play.structure.Tree;

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
