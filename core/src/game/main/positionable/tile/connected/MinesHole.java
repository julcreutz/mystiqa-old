package game.main.positionable.tile.connected;

import game.main.positionable.Hitbox;
import game.resource.SpriteSheet;

public class MinesHole extends ConnectedTile {
    public MinesHole() {
        hitbox = new Hitbox(this, 0, 0, 8, 8);
        isOverfliable = true;
        connectWith = new Class[] {MinesWall.class, SpiderNestBridge.class};
        spriteSheet = new SpriteSheet("tiles/mines_hole", 5, 4);
    }
}
