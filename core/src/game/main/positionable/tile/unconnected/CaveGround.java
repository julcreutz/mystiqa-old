package game.main.positionable.tile.unconnected;

import game.resource.SpriteSheet;

public class CaveGround extends UnconnectedTile {
    public CaveGround() {
        spriteSheet = new SpriteSheet("tiles/cave_ground", 1, 1);
    }
}
