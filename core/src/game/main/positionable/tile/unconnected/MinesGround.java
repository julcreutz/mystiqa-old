package game.main.positionable.tile.unconnected;

import game.resource.SpriteSheet;

public class MinesGround extends UnconnectedTile {
    public MinesGround() {
        spriteSheet = new SpriteSheet("tiles/mines_ground", 1, 1);
    }
}
