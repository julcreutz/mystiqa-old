package game.loader.instance;

import game.main.play.structure.SingleTile;
import game.main.play.structure.Structure;
import game.main.play.structure.Tree;

public class StructureLoader extends InstanceLoader<Structure> {
    @Override
    public Structure newInstance(String name) {
        Structure s = null;

        switch (name) {
            case "Tree":
                s = new Tree();
                break;
            case "SingleTile":
                s = new SingleTile();
                break;
        }

        return s;
    }
}
