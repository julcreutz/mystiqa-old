package game.main.positionable.tile.connected;

import game.resource.SpriteSheet;

public class SpiderNestSpiderWeb extends ConnectedTile {
    public SpiderNestSpiderWeb() {
        spriteSheet = new SpriteSheet("tiles/spider_nest_spider_web", 5, 4);
        moveSpeed = .5f;
    }
}
