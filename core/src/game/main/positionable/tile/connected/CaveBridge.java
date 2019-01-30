package game.main.positionable.tile.connected;

import game.SpriteSheet;
import game.main.positionable.tile.unconnected.CaveGround;
import game.main.positionable.tile.unconnected.CaveMushroom;
import game.main.positionable.tile.unconnected.CaveRock;

public class CaveBridge extends ConnectedTile {
    public CaveBridge() {
        connectWith = new Class[] {CaveGround.class, CaveMushroom.class, CaveRock.class};
        spriteSheet = new SpriteSheet("cave_bridge", 5, 4);
    }
}
