package mystiqa.entity.tile;

import mystiqa.Resources;
import mystiqa.color.Green;
import mystiqa.color.LightGray;

public class Hill extends Tile {
    public Hill() {
        topGraphics = Resources.getSpriteSheet("graphics/entities/tiles/grass_top.png", 8, 8);
        sideGraphics = Resources.getSpriteSheet("graphics/entities/tiles/grass_side.png", 8, 8);

        topColor = new Green();
        sideColor = new LightGray();
    }
}
