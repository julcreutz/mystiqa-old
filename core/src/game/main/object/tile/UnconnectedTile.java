package game.main.object.tile;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;
import game.main.state.play.map.Map;

public class UnconnectedTile extends Tile {
    public TextureRegion[][] spriteSheet;

    @Override
    public void update(Map map) {
        super.update(map);

        if (image == null) {
            image = spriteSheet[Game.RANDOM.nextInt(spriteSheet.length)][Game.RANDOM.nextInt(spriteSheet[0].length)];
        }
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue spriteSheet = json.get("spriteSheet");
        if (spriteSheet != null) {
            this.spriteSheet = Game.SPRITE_SHEETS.load(spriteSheet.asString()).sheet;
        }
    }
}
