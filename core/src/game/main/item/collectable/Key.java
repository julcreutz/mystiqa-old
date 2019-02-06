package game.main.item.collectable;

import game.resource.SpriteSheet;

public class Key extends Collectable {
    public Key() {
        icon = new SpriteSheet("items/collectables/key");
    }

    @Override
    public String getName() {
        return "Key";
    }
}
