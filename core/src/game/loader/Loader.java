package game.loader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.ObjectMap;

/**
 * Defines a class able to load and handle serialization requests. Note that only classes that implement
 * {@link Serializable} are able to be serialized.
 *
 * For serializing, {@link JsonReader} is used.
 */
public abstract class Loader<T extends Serializable> {
    /** Static reader instance for easy access. */
    public static JsonReader READER = new JsonReader();

    public String root;
    public ObjectMap<String, FileHandle> files;

    public Loader(String root) {
        this.root = root;
        files = new ObjectMap<String, FileHandle>();
    }

    public T load(String name) {
        if (!files.containsKey(name)) {
            Array<FileHandle> files = new Array<FileHandle>();
            getFiles(Gdx.files.internal(root), files);

            for (FileHandle file : files) {
                if (file.nameWithoutExtension().equals(name)) {
                    this.files.put(name, file);
                    break;
                }
            }
        }

        return load(files.get(name));
    }

    public void getFiles(FileHandle file, Array<FileHandle> files) {
        if (file.isDirectory()) {
            for (FileHandle _file : file.list()) {
                getFiles(_file, files);
            }
        } else {
            files.add(file);
        }
    }

    /**
     * Loads given file and deserializes all objects within. Storing is handled by respective implementation.
     *
     * @param file file to be deserialized
     */
    public abstract T load(FileHandle file);
}
