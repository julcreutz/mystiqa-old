package game.main.positionable.tile.unconnected;

import game.SpriteSheet;
import game.main.Game;
import game.main.state.play.map.Map;
import game.main.positionable.tile.Tile;

public class UnconnectedTile extends Tile {
    public SpriteSheet spriteSheet;

    @Override
    public void update(Map map) {
        super.update(map);

        if (image == null) {
            image = spriteSheet.grab(Game.RANDOM.nextInt(spriteSheet.getColumns()),
                    Game.RANDOM.nextInt(spriteSheet.getRows()));
        }
    }
}
