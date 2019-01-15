package game.loader.resource;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import game.loader.Loader;
import game.loader.Serializable;

import java.util.HashMap;

public abstract class ResourceLoader<T extends Serializable> extends Loader<T> {
    public ObjectMap<FileHandle, T> map;

    public ResourceLoader(String root) {
        super(root);
        map = new ObjectMap<FileHandle, T>();
    }

    @Override
    public T load(FileHandle file) {
        if (!map.containsKey(file)) {
            T t = newInstance();
            t.deserialize(Loader.READER.parse(file));
            map.put(file, t);
        }

        return map.get(file);
    }

    public Array<T> loadAll() {
        Array<T> all = new Array<T>();

        for (T t : map.values()) {
            all.add(t);
        }

        return all;
    }

    public abstract T newInstance();
}
