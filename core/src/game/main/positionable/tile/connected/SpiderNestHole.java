package game.main.positionable.tile.connected;

import game.resource.SpriteSheet;
import game.main.positionable.Hitbox;

public class SpiderNestHole extends ConnectedTile {
    public SpiderNestHole() {
        hitbox = new Hitbox(this, 0, 0, 8, 8);
        isOverfliable = true;
        connectWith = new Class[] {SpiderNestWall.class, SpiderNestBridge.class};
        spriteSheet = new SpriteSheet("tiles/spider_nest_hole", 5, 4);
    }
}
