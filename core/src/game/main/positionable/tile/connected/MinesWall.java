package game.main.positionable.tile.connected;

import game.main.positionable.Hitbox;
import game.resource.SpriteSheet;

public class MinesWall extends ConnectedTile {
    public MinesWall() {
        hitbox = new Hitbox(this, 0, 0, 8, 8);
        spriteSheet = new SpriteSheet("tiles/mines_wall", 5, 4);
    }
}
