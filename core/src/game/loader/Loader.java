package game.loader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public interface Loader<T extends Serializable> {
    JsonReader READER = new JsonReader();

    void load(FileHandle file);
    T load(String name);
}
