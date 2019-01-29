package game.resource;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.ObjectMap;

public class Shaders implements Disposable {
    private ObjectMap<FileHandle, ShaderProgram> shaders;

    public Shaders() {
        shaders = new ObjectMap<FileHandle, ShaderProgram>();
    }

    public ShaderProgram load(String path) {
        FileHandle file = Gdx.files.internal("shaders/" + path);

        if (!shaders.containsKey(file)) {
            shaders.put(file, new ShaderProgram(file.child(file.nameWithoutExtension() + ".vert"),
                    file.child(file.nameWithoutExtension() + ".frag")));
        }

        return shaders.get(file);
    }

    @Override
    public void dispose() {
        for (ShaderProgram shader : shaders.values()) {
            shader.dispose();
        }
    }
}
