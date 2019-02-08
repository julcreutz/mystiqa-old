package game.main.positionable.tile.connected;

import game.resource.SpriteSheet;
import game.main.positionable.Hitbox;

public class SpiderNestWall extends ConnectedTile {
    public SpiderNestWall() {
        hitbox = new Hitbox(this, 0, 0, 8, 8);
        spriteSheet = new SpriteSheet("tiles/spider_nest_wall", 5, 4);
    }
}
