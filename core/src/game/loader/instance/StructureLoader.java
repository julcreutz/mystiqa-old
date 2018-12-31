package game.loader.instance;

import game.main.state.play.map.structure.*;

public class StructureLoader extends InstanceLoader<Structure> {
    @Override
    public Structure newInstance(String name) {
        if (name.equals("Tree")) {
            return new Tree();
        } else if (name.equals("SingleTile")) {
            return new SingleTile();
        } else if (name.equals("HouseExterior")) {
            return new HouseExterior();
        } else if (name.equals("Village")) {
            return new Village();
        }

        return null;
    }
}
