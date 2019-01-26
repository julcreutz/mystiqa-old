package game.main.object.tile;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.reference.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.state.play.map.Map;

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

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue spriteSheet = json.get("spriteSheet");
        if (spriteSheet != null) {
            this.spriteSheet = Game.SPRITE_SHEETS.load(spriteSheet.asString());
        }
    }
}
