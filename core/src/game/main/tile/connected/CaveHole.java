package game.main.tile.connected;

import game.SpriteSheet;

public class CaveHole extends ConnectedTile {
    public CaveHole() {
        solid = true;
        connectWith = new Class[] {CaveWall.class, CaveBridge.class};
        spriteSheet = new SpriteSheet("cave_hole", 5, 4);
    }
}
