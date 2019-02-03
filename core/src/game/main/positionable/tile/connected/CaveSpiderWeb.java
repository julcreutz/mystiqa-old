package game.main.positionable.tile.connected;

import game.resource.SpriteSheet;

public class CaveSpiderWeb extends ConnectedTile {
    public CaveSpiderWeb() {
        spriteSheet = new SpriteSheet("spider_web", 5, 4);
        moveSpeed = .5f;
    }
}
