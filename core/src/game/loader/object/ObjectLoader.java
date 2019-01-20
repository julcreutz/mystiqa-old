package game.loader.object;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import game.loader.Loader;
import game.loader.Serializable;

public abstract class ObjectLoader<T extends Serializable> implements Loader<T> {
    public ObjectMap<String, JsonValue> map;

    @Override
    public void load(FileHandle file) {
        map = new ObjectMap<String, JsonValue>();

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
