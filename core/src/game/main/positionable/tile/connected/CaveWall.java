package game.main.positionable.tile.connected;

import game.resource.SpriteSheet;
import game.main.positionable.Hitbox;

public class CaveWall extends ConnectedTile {
    public CaveWall() {
        hitbox = new Hitbox(this, 0, 0, 8, 8);
        spriteSheet = new SpriteSheet("tiles/cave_wall", 5, 4);
    }
}
