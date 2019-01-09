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
    c.r = 1;
    c.g = 1;
    c.b = 1;
  }

  gl_FragColor = c;
}