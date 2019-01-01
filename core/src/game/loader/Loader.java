package game.loader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

/**
 * Defines a class able to load and handle serialization requests. Note that
 * only classes that implement {@link Serializable} are able to be serialized.
 * For serializing, libGDX's {@link JsonReader} is used.
 */
public interface Loader<T extends Serializable> {
    /** Static reader instance for easy access. */
    JsonReader READER = new JsonReader();

    /**
     * Loads given file and deserializes all objects within. Storing is handled
     * by respective implementation.
     *
     * @param file file to be deserialized
     */
    void load(FileHandle file);

    /**
     * Loads and returns the object corresponding to given name. This is handled
     * differently per implementation.
     *
     * @param name name of object
     * @return deserialized object
     */
    T load(String name);
}
