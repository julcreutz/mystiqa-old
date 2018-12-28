package game.main.item.equipment.armor;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.item.equipment.Equipment;

public abstract class Armor extends Equipment {
    public SpriteSheet spriteSheet;

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("spriteSheet")) {
            spriteSheet = Game.SPRITE_SHEETS.load(json.getString("spriteSheet"));
        }
    }
}
