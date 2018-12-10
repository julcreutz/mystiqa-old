package game.loader.palette;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import game.loader.ColorLoader;

public class PaletteShaderLoader {
    private static Array<PaletteShader> shaders;

    public static ShaderProgram load(String[] colors) {
        if (shaders == null) {
            shaders = new Array<PaletteShader>();
        }

        for (PaletteShader shader : shaders) {
            if (shader.same(colors)) {
                return shader.shader;
            }
        }

        Color[] _colors = new Color[colors.length];

        for (int i = 0; i < colors.length; i++) {
            _colors[i] = ColorLoader.load(colors[i]);
        }

        PaletteShader shader = new PaletteShader();
        shader.colors = colors;

        String frag = null;

        if (colors.length == 2) {
            frag = "#ifdef GL_ES\n" +
                    "    precision mediump float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec4 c = v_color * texture2D(u_texture, v_texCoords);\n" +
                    "\n" +
                    "    if (c.w > 0) {\n" +
                    "        if (c.r == 0.0 && c.g == 0.0 && c.b == 0.0) {\n" +
                    "            c.r = " + _colors[0].r + ";\n" +
                    "            c.g = " + _colors[0].g + ";\n" +
                    "            c.b = " + _colors[0].b + ";\n" +
                    "        }\n" +
                    "        if (c.r == 1.0 && c.g == 1.0 && c.b == 1.0) {\n" +
                    "            c.r = " + _colors[1].r + ";\n" +
                    "            c.g = " + _colors[1].g + ";\n" +
                    "            c.b = " + _colors[1].b + ";\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    gl_FragColor = c;\n" +
                    "}";
        } else if (colors.length == 3) {
            frag = "#ifdef GL_ES\n" +
                    "    precision mediump float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec4 c = v_color * texture2D(u_texture, v_texCoords);\n" +
                    "\n" +
                    "    if (c.w > 0) {\n" +
                    "        if (c.r == 0.0 && c.g == 0.0 && c.b == 0.0) {\n" +
                    "            c.r = " + _colors[0].r + ";\n" +
                    "            c.g = " + _colors[0].g + ";\n" +
                    "            c.b = " + _colors[0].b + ";\n" +
                    "        }\n" +
                    "        if (c.r == 0.5 && c.g == 0.5 && c.b == 0.5) {\n" +
                    "            c.r = " + _colors[1].r + ";\n" +
                    "            c.g = " + _colors[1].g + ";\n" +
                    "            c.b = " + _colors[1].b + ";\n" +
                    "        }\n" +
                    "        if (c.r == 1.0 && c.g == 1.0 && c.b == 1.0) {\n" +
                    "            c.r = " + _colors[2].r + ";\n" +
                    "            c.g = " + _colors[2].g + ";\n" +
                    "            c.b = " + _colors[2].b + ";\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    gl_FragColor = c;\n" +
                    "}";
        } else if (colors.length == 5) {
            frag = "#ifdef GL_ES\n" +
                    "    precision mediump float;\n" +
                    "#endif\n" +
                    "\n" +
                    "varying vec4 v_color;\n" +
                    "varying vec2 v_texCoords;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "\n" +
                    "void main() {\n" +
                    "    vec4 c = v_color * texture2D(u_texture, v_texCoords);\n" +
                    "\n" +
                    "    if (c.w > 0) {\n" +
                    "        if (c.r == 0.0 && c.g == 0.0 && c.b == 0.0) {\n" +
                    "            c.r = " + _colors[0].r + ";\n" +
                    "            c.g = " + _colors[0].g + ";\n" +
                    "            c.b = " + _colors[0].b + ";\n" +
                    "        }\n" +
                    "        if (c.r == 0.25 && c.g == 0.25 && c.b == 0.25) {\n" +
                    "            c.r = " + _colors[1].r + ";\n" +
                    "            c.g = " + _colors[1].g + ";\n" +
                    "            c.b = " + _colors[1].b + ";\n" +
                    "        }\n" +
                    "        if (c.r == 0.5 && c.g == 0.5 && c.b == 0.5) {\n" +
                    "            c.r = " + _colors[2].r + ";\n" +
                    "            c.g = " + _colors[2].g + ";\n" +
                    "            c.b = " + _colors[2].b + ";\n" +
                    "        }\n" +
                    "        if (c.r == 0.75 && c.g == 0.75 && c.b == 0.75) {\n" +
                    "            c.r = " + _colors[3].r + ";\n" +
                    "            c.g = " + _colors[3].g + ";\n" +
                    "            c.b = " + _colors[3].b + ";\n" +
                    "        }\n" +
                    "        if (c.r == 1.0 && c.g == 1.0 && c.b == 1.0) {\n" +
                    "            c.r = " + _colors[4].r + ";\n" +
                    "            c.g = " + _colors[4].g + ";\n" +
                    "            c.b = " + _colors[4].b + ";\n" +
                    "        }\n" +
                    "    }\n" +
                    "\n" +
                    "    gl_FragColor = c;\n" +
                    "}";
        }

        shader.shader = new ShaderProgram("attribute vec4 a_position;\n" +
                "attribute vec4 a_color;\n" +
                "attribute vec2 a_texCoord0;\n" +
                "\n" +
                "uniform mat4 u_projTrans;\n" +
                "\n" +
                "varying vec4 v_color;\n" +
                "varying vec2 v_texCoords;\n" +
                "\n" +
                "void main() {\n" +
                "    v_color = a_color;\n" +
                "    v_texCoords = a_texCoord0;\n" +
                "\n" +
                "    gl_Position = u_projTrans * a_position;\n" +
                "}", frag);

        shaders.add(shader);

        return shader.shader;
    }
}
