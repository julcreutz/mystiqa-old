package game.loader.palette;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;

public class PaletteShader {
    public String[] colors;
    public ShaderProgram shader;

    public boolean same(String[] colors) {
        if (this.colors.length != colors.length) {
            return false;
        }

        for (String color : this.colors) {
            boolean has = false;

            for (String _color : colors) {
                if (color.equals(_color)) {
                    has = true;
                }
            }

            if (!has) {
                return false;
            }
        }

        return true;
    }
}
