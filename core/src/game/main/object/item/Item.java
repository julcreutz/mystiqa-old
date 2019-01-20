package game.main.object.item;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.object.GameObject;
import game.main.object.item.equipment.armor.Armor;
import game.main.object.item.equipment.hand.left.Shield;
import game.main.object.item.equipment.hand.right.MeleeWeapon;

/** Superclass every type of item, e.g. weapons or consumables, must extend. Only consists of name and icon. */
public abstract class Item extends GameObject {
    public enum Type {
        MELEE_WEAPON, SHIELD, ARMOR;

        public Item newInstance() {
            switch (this) {
                case MELEE_WEAPON:
                    return new MeleeWeapon();
                case SHIELD:
                    return new Shield();
                case ARMOR:
                    return new Armor();
            }

            return null;
        }
    }

    /** Name of item. */
    public String name;
    /** Item icon. */
    public SpriteSheet icon;

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue name = json.get("name");
        if (name != null) {
            this.name = name.asString();
        }

        JsonValue icon = json.get("icon");
        if (icon != null) {
            this.icon = Game.SPRITE_SHEETS.load(icon.asString());
        }
    }
}
