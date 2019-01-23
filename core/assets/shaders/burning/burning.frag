#ifdef GL_ES
precision mediump float;
#endif

varying vec4 v_color;
varying vec2 v_texCoords;

uniform sampler2D u_texture;

void main()
{
  vec4 c = v_color * texture2D(u_texture, v_texCoords);

  if (c.w > 0) {
    c.r += 191.0 / 255.0;
    c.g += 48.0 / 255.0;
    c.b += 16.0 / 255.0;
  }

  gl_FragColor = c;
}