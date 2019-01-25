package game.loader.resource.sprite_sheet;

import com.badlogic.gdx.utils.Disposable;
import game.loader.resource.ResourceLoader;

public class SpriteSheetLoader extends ResourceLoader<SpriteSheet> implements Disposable {
    @Override
    public SpriteSheet newInstance() {
        return new SpriteSheet();
    }

    @Override
    public void dispose() {
        for (SpriteSheet spriteSheet : loadAll()) {
            spriteSheet.getSource().dispose();
        }
    }
}
