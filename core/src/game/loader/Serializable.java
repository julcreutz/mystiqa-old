package game.loader;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Defines a class able to be serialized into .json format.
 *
 * Serialization is using the {@link com.badlogic.gdx.utils.Json} implementation.
 */
public interface Serializable {
    /**
     * Deserializes the object with given json file.
     *
     * @param json json value to deserialize
     */
    void deserialize(JsonValue json);
}
