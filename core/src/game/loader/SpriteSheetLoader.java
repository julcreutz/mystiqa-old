package game.loader;

import game.main.SpriteSheet;

public class SpriteSheetLoader extends ResourceLoader<SpriteSheet> {
    @Override
    public SpriteSheet newInstance() {
        return new SpriteSheet();
    }
}
