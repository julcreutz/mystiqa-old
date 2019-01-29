package game.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class Textures implements Disposable {
    private ObjectMap<FileHandle, Texture> textures;

    public Textures() {
        textures = new ObjectMap<FileHandle, Texture>();
    }

    public Texture load(String path) {
        FileHandle file = Gdx.files.internal("textures/" + path + ".png");

        if (!textures.containsKey(file)) {
            textures.put(file, new Texture(file));
        }

        return textures.get(file);
    }

    @Override
    public void dispose() {
        for (Texture t : textures.values()) {
            t.dispose();
        }
    }
}
