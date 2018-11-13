package mystiqa.entity.being.humanoid;

import mystiqa.Resources;
import mystiqa.color.LightPink;
import mystiqa.stat.MaxHealth;

public class Human extends Humanoid {
    public Human() {
        feet = Resources.getSpriteSheet("graphics/entities/beings/humanoid/human/human_feet.png", 8, 8);
        body = Resources.getSpriteSheet("graphics/entities/beings/humanoid/human/human_body.png", 8, 8);
        head = Resources.getSpriteSheet("graphics/entities/beings/humanoid/human/human_head.png", 8, 8);

        color = new LightPink();

        stats.add(new MaxHealth(16));
    }
}
