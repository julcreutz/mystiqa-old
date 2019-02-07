package game.main.positionable.tile.connected;

import game.main.positionable.Hitbox;
import game.resource.SpriteSheet;

public class ForestTrees extends ConnectedTile {
    public ForestTrees() {
        hitbox = new Hitbox(this, 0, 0, 8, 8);
        spriteSheet = new SpriteSheet("tiles/forest_trees", 5, 4);
    }
}
