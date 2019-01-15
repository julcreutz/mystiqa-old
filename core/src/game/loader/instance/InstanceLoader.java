package game.loader.instance;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.ObjectMap;
import game.loader.Loader;
import game.loader.Serializable;

import java.util.HashMap;

public abstract class InstanceLoader<T extends Serializable> extends Loader<T> {
    public ObjectMap<FileHandle, JsonValue> map;

    public InstanceLoader(String root) {
        super(root);
        map = new ObjectMap<FileHandle, JsonValue>();
    }

    @Override
    public T load(FileHandle file) {
        if (!map.containsKey(file)) {
            map.put(file, Loader.READER.parse(file));
        }

        JsonValue json = map.get(file);
        T t = newInstance(json.child.name);
        t.deserialize(json.child);

        return t;
    }

    public abstract T newInstance(String name);
}
