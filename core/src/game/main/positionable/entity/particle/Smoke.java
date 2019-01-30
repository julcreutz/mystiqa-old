package game.main.positionable.entity.particle;

import com.badlogic.gdx.math.MathUtils;
import game.SpriteSheet;
import game.main.Game;

public class Smoke extends Particle {
    private float dir;
    private float speed;

    public Smoke() {
        image = new SpriteSheet("particle_circle").grab(0, 0);

        dir = MathUtils.random(0f, 360f);
        speed = MathUtils.random(8f, 32f);

        scale = MathUtils.random(0.5f, 0.75f);
    }

    @Override
    public void update() {
        super.update();

        velX += MathUtils.cosDeg(dir) * speed;
        velY += MathUtils.sinDeg(dir) * speed;

        scale -= Game.getDelta();
    }
}
