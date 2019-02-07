package game.main.positionable.tile.connected;

import game.main.positionable.Hitbox;
import game.resource.SpriteSheet;

public class ForestWater extends ConnectedTile {
    public ForestWater() {
        hitbox = new Hitbox(this, 0, 0, 8, 8);
        spriteSheet = new SpriteSheet("tiles/forest_water", 5, 4);
    }
}
