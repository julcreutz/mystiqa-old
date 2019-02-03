package game.main.item.collectable;

import game.resource.SpriteSheet;

public class Key extends Collectable {
    public Key() {
        icon = new SpriteSheet("key");
    }

    @Override
    public String getName() {
        return "Key";
    }
}
