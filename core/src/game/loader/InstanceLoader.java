package game.loader;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;

import java.util.HashMap;

public abstract class InstanceLoader<T extends Serializable> implements Loader<T> {
    public HashMap<String, JsonValue> map;

    @Override
    public void load(FileHandle file) {
        map = new HashMap<>();

        for (JsonValue json : Loader.READER.parse(file)) {
            map.put(json.name, json.child);
        }
    }

    @Override
    public T load(String name) {
        T t = newInstance(map.get(name).name);
        t.deserialize(map.get(name));
        return t;
    }

    public abstract T newInstance(String name);
}
