package game.main.positionable.tile.unconnected;

import game.resource.SpriteSheet;

public class CaveRock extends UnconnectedTile {
    public CaveRock() {
        spriteSheet = new SpriteSheet("tiles/cave_rock", 2, 1);
    }
}
