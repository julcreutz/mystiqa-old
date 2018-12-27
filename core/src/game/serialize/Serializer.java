package game.serialize;

import com.badlogic.gdx.utils.JsonValue;

import java.lang.reflect.Field;

public class Serializer {
    public <T> T deserialize(JsonValue json, Class<T> c) {
        try {
            T t = c.newInstance();

            for (Field f : c.getDeclaredFields()) {
                String name = f.getName();

                if (json.has(name)) {
                    if (f.isAnnotationPresent(Serialize.class)) {
                        String type = f.getType().getName();

                        if (type.equals("int")) {
                            f.setInt(t, json.getInt(name));
                        } else if (type.equals("float")) {
                            f.setFloat(t, json.getFloat(name));
                        } else if (type.equals("boolean")) {
                            f.setBoolean(t, json.getBoolean(name));
                        } else if (type.equals("java.lang.String")) {
                            f.set(t, json.getString(name));
                        } else {
                            f.set(t, deserialize(json.get(name), Class.forName(type)));
                        }
                    }
                }
            }

            return t;
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
