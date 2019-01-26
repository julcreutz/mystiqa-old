package game.loader.reference.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class Shader implements Serializable {
    public ShaderProgram shader;

    @Override
    public void deserialize(JsonValue json) {
        shader = new ShaderProgram(Gdx.files.internal(json.getString("vertexPath")),
                Gdx.files.internal(json.getString("fragmentPath")));
    }
}
