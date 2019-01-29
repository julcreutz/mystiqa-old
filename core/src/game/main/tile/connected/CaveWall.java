package game.main.tile.connected;

import game.SpriteSheet;

public class CaveWall extends ConnectedTile {
    public CaveWall() {
        solid = true;
        spriteSheet = new SpriteSheet("cave_wall", 5, 4);
    }
}
