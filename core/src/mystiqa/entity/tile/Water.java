package mystiqa.entity.tile;

import mystiqa.Resources;
import mystiqa.color.Blue;
import mystiqa.color.Brown;
import mystiqa.color.Green;

public class Water extends Tile {
    public Water() {
        topGraphics = Resources.getSpriteSheet("graphics/entities/tiles/water.png", 8, 8);

        topColor = new Blue();
    }
}
