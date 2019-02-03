package game.main.positionable.entity.particle;

import com.badlogic.gdx.math.MathUtils;
import game.resource.SpriteSheet;
import game.main.Game;

public class Flame extends Particle {
    public Flame() {
        image = new SpriteSheet("flame").grab(0, 0);
        scale = 1;
        rot = -90;
    }

    @Override
    public void update() {
        super.update();

        scale -= 2 * Game.getDelta();

        velX += MathUtils.cosDeg(90) * 24f;
        velY += MathUtils.sinDeg(90) * 24f;
    }
}
