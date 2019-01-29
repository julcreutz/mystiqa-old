package game.main.tile.connected;

import game.SpriteSheet;
import game.main.tile.unconnected.CaveGround;

public class CaveBridge extends ConnectedTile {
    public CaveBridge() {
        connectWith = new Class[] {CaveGround.class};
        spriteSheet = new SpriteSheet("cave_bridge", 5, 4);
    }
}
