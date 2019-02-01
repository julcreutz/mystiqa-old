package game.main.positionable.tile.connected;

import game.SpriteSheet;

public class SpiderWeb extends ConnectedTile {
    public SpiderWeb() {
        spriteSheet = new SpriteSheet("spider_web", 5, 4);
        moveSpeed = .5f;
    }
}
