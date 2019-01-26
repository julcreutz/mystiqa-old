package game.loader.reference.shader;

import game.loader.reference.ReferenceLoader;

public class ShaderLoader extends ReferenceLoader<Shader> {
    @Override
    public Shader newInstance() {
        return new Shader();
    }
}
