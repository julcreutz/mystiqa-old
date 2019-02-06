package game.main.positionable.tile.connected;

import game.resource.SpriteSheet;
import game.main.positionable.Hitbox;

public class CaveHole extends ConnectedTile {
    public CaveHole() {
        hitbox = new Hitbox(this, 0, 0, 8, 8);
        isOverfliable = true;
        connectWith = new Class[] {CaveWall.class, CaveBridge.class};
        spriteSheet = new SpriteSheet("tiles/cave_hole", 5, 4);
    }
}
