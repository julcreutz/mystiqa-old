package game.main.state.play.map.dungeon.lock;

import game.main.Game;
import game.main.state.play.map.dungeon.Dungeon;
import game.main.state.play.map.entity.Block;

public class PushBlockLock extends Lock {
    public Block block;

    @Override
    public void onLock() {
        super.onLock();

        for (int x = room.x0(); x < room.x1(); x++) {
            for (int y = room.y0(); y < room.y1(); y++) {
                if (dungeon.tiles.at(x, y, 0).name.equals(dungeon.innerWall)) {
                    dungeon.tiles.set(Game.TILES.load(dungeon.ground), x, y, 0);

                    block = (Block) Game.ENTITIES.load("DungeonBlock");

                    block.x = x * 8;
                    block.y = y * 8;

                    dungeon.entities.addEntity(block);

                    return;
                }
            }
        }
    }

    @Override
    public boolean isRoomValid(Dungeon.Room r) {
        for (int x = r.x0(); x < r.x1(); x++) {
            for (int y = r.y0(); y < r.y1(); y++) {
                if (dungeon.tiles.at(x, y, 0).name.equals(dungeon.innerWall)) {
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean isLocked() {
        return block != null && block.getStartDistance() < 8;
    }
}
