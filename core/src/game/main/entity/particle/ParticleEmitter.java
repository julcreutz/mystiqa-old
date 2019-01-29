package game.main.entity.particle;

import com.badlogic.gdx.math.MathUtils;
import game.main.Game;
import game.main.state.play.map.Map;

public class ParticleEmitter {
    public Map map;

    public Class particle;

    public float x;
    public float y;

    public float minDir;
    public float maxDir;

    public float minOffset;
    public float maxOffset;

    public float minTime;
    public float maxTime;

    public float time;

    public void update() {
        time -= Game.getDelta();

        if (time < 0) {
            time = MathUtils.random(minTime, maxTime);

            try {
                Particle p = (Particle) particle.newInstance();

                float dir = MathUtils.random(minDir, maxDir);
                float offset = MathUtils.random(minOffset, maxOffset);

                p.x = x + MathUtils.cosDeg(dir) * offset;
                p.y = y + MathUtils.sinDeg(dir) * offset;

                map.entities.addEntity(p);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
