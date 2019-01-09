package game.loader.resource.shader;

import game.loader.resource.ResourceLoader;

public class ShaderLoader extends ResourceLoader<Shader> {
    @Override
    public Shader newInstance() {
        return new Shader();
    }
}
