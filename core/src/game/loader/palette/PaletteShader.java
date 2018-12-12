package game.loader.palette;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PaletteShader {
    public String[] colors;
    public ShaderProgram shader;

    public boolean same(String[] colors) {
        if (this.colors.length != colors.length) {
            return false;
        }

        for (int i = 0; i < colors.length; i++) {
            if (!colors[i].equals(this.colors[i])) {
                return false;
            }
        }

        return true;
    }
}
