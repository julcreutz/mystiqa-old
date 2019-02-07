package game.main.positionable.tile.connected;

import game.resource.SpriteSheet;

public class TallGrass extends ConnectedTile {
    public TallGrass() {
        spriteSheet = new SpriteSheet("tiles/tall_grass", 5, 4);
        moveSpeed = .75f;
        overlay = new Overlay();
        overlay.image = new SpriteSheet("tiles/tall_grass_overlay").grab(0, 0);
    }
}
