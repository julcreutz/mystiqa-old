package game.loader;

import com.badlogic.gdx.utils.JsonValue;

/**
 * Defines a class able to be serialized into .json format.
 * Serialization is using libGDX's JSON implementation, see
 * {@link com.badlogic.gdx.utils.Json} for more details.
 */
public interface Serializable {
    /** Deserializes the object with given json file. */
    void deserialize(JsonValue json);
}
