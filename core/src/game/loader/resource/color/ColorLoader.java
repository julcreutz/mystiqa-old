package game.loader.resource.color;

import game.loader.resource.ResourceLoader;
import game.loader.resource.color.ColorResource;

public class ColorLoader extends ResourceLoader<ColorResource> {
    @Override
    public ColorResource newInstance() {
        return new ColorResource();
    }
}
