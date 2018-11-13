package mystiqa.entity.tile;

import mystiqa.Resources;
import mystiqa.color.Brown;
import mystiqa.color.Green;

public class Grass extends Tile {
    public Grass() {
        topGraphics = Resources.getSpriteSheet("graphics/entities/tiles/grass_top.png", 8, 8);
        sideGraphics = Resources.getSpriteSheet("graphics/entities/tiles/grass_side.png", 8, 8);

        topColor = new Green();
        sideColor = new Brown();
    }
}
