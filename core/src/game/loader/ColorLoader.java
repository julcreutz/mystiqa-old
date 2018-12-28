package game.loader;

import game.main.Color;

public class ColorLoader extends ResourceLoader<Color> {
    @Override
    public Color newInstance() {
        return new Color();
    }
}
