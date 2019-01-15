package game.loader.resource.sprite_sheet;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Disposable;
import game.loader.resource.ResourceLoader;

public class SpriteSheetLoader extends ResourceLoader<SpriteSheet> implements Disposable {
    public SpriteSheetLoader(String root) {
        super(root);
    }

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
