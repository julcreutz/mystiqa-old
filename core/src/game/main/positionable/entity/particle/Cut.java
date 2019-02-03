package game.main.positionable.entity.particle;

import com.badlogic.gdx.math.MathUtils;
import game.resource.SpriteSheet;
import game.main.Game;

public class Cut extends Particle {
    public Cut(float rot) {
        image = new SpriteSheet("particle_line").grab(0, 0);
        this.rot = rot;
        scale = MathUtils.random(1.5f, 2f);
    }

    @Override
    public void update() {
        super.update();

        scale -= 15 * Game.getDelta();
    }
}
