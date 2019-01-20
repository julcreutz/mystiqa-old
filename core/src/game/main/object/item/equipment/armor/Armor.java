package game.main.object.item.equipment.armor;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.object.item.equipment.Equipment;

public class Armor extends Equipment {
    public SpriteSheet feet;
    public SpriteSheet body;
    public SpriteSheet head;

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue feet = json.get("feet");
        if (feet != null) {
            this.feet = Game.SPRITE_SHEETS.load(feet.asString());
        }

        JsonValue body = json.get("body");
        if (body != null) {
            this.body = Game.SPRITE_SHEETS.load(body.asString());
        }

        JsonValue head = json.get("head");
        if (head != null) {
            this.head = Game.SPRITE_SHEETS.load(head.asString());
        }
    }
}
