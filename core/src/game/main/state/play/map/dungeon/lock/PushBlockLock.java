package game.main.state.play.map.dungeon.lock;

import game.main.state.play.map.dungeon.Dungeon;
import game.main.entity.Block;
import game.main.entity.Entity;

public class PushBlockLock extends Lock {
    public Block block;

    @Override
    public void onLock() {
        super.onLock();

        for (Entity e : room.getContainedEntities()) {
            if (e instanceof Block) {
                block = (Block) e;
                break;
            }
        }
    }

    @Override
    public boolean isLocked() {
        return block != null && block.getStartDistance() == 0;
    }

    @Override
    public boolean isValid(Dungeon.Template t) {
        for (int x = 0; x < t.template.length; x++) {
            for (int y = 0; y < t.template[0].length; y++) {
                if (t.template[x][y] == 'p') {
                    return true;
                }
            }
        }

        return false;
    }
}
