package game.loader.reference.sprite_sheet;

import com.badlogic.gdx.utils.Disposable;
import game.loader.reference.ReferenceLoader;

public class SpriteSheetLoader extends ReferenceLoader<SpriteSheet> implements Disposable {
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
