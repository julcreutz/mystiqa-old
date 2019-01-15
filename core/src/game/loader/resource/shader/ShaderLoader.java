package game.loader.resource.shader;

import com.badlogic.gdx.files.FileHandle;
import game.loader.resource.ResourceLoader;

public class ShaderLoader extends ResourceLoader<Shader> {
    public ShaderLoader(String root) {
        super(root);
    }

    @Override
    public Shader newInstance() {
        return new Shader();
    }
}
