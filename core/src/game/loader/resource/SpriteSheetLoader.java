package game.loader.resource;

import com.badlogic.gdx.utils.Disposable;
import game.main.SpriteSheet;

public class SpriteSheetLoader extends ResourceLoader<SpriteSheet> implements Disposable {
    @Override
    public SpriteSheet newInstance() {
        return new SpriteSheet();
    }

    @Override
    public void dispose() {
        for (SpriteSheet spriteSheet : loadAll()) {
            spriteSheet.texture.dispose();
        }
    }
}
