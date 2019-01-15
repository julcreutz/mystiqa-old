package game.loader.resource;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Loader;
import game.loader.Serializable;

import java.util.HashMap;

public abstract class ResourceLoader<T extends Serializable> implements Loader<T> {
    public HashMap<String, T> map;

    @Override
    public void load(FileHandle file) {
        map = new HashMap<String, T>();

        for (JsonValue json : Loader.READER.parse(file)) {
            T t = newInstance();
            t.deserialize(json);
            map.put(json.name, t);
        }
    }

    @Override
    public T load(String name) {
        return map.containsKey(name) ? map.get(name) : null;
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
