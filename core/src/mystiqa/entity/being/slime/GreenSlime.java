package mystiqa.entity.being.slime;

import mystiqa.Resources;
import mystiqa.color.Green;

public class GreenSlime extends Slime {
    public GreenSlime() {
        graphics = Resources.getSpriteSheet("graphics/entities/beings/slime/slime.png", 8, 8);
        color = new Green();
    }
}
