package game.main.positionable.tile.connected;

import game.resource.SpriteSheet;
import game.main.positionable.tile.unconnected.SpiderNestGround;
import game.main.positionable.tile.unconnected.SpiderNestMushroom;
import game.main.positionable.tile.unconnected.SpiderNestRock;

public class SpiderNestBridge extends ConnectedTile {
    public SpiderNestBridge() {
        connectWith = new Class[] {SpiderNestGround.class, SpiderNestMushroom.class, SpiderNestRock.class, SpiderNestSpiderWeb.class};
        spriteSheet = new SpriteSheet("tiles/spider_nest_bridge", 5, 4);
    }
}
