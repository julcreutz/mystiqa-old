package game.main.object.entity.particle;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.object.entity.Entity;
import game.main.state.play.map.Map;

public class ParticleEmitter implements Serializable {
    public Map map;

    public String particle;

    public float x;
    public float y;

    public float minDir;
    public float maxDir;

    public float minOffset;
    public float maxOffset;

    public float minTime;
    public float maxTime;

    public float time;

    public ParticleEmitter(JsonValue json) {
        deserialize(json);
    }

    public void update() {
        time -= Game.getDelta();

        if (time < 0) {
            time = MathUtils.random(minTime, maxTime);

            Particle p = (Particle) Game.ENTITIES.load(particle);

            float dir = MathUtils.random(minDir, maxDir);
            float offset = MathUtils.random(minOffset, maxOffset);

            p.x = x + MathUtils.cosDeg(dir) * offset;
            p.y = y + MathUtils.sinDeg(dir) * offset;

            map.entities.addEntity(p);
        }
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue particle = json.get("particle");
        if (particle != null) {
            this.particle = particle.asString();
        }

        JsonValue minDir = json.get("minDir");
        if (minDir != null) {
            this.minDir = minDir.asFloat();
        }

        JsonValue maxDir = json.get("maxDir");
        if (maxDir != null) {
            this.maxDir = maxDir.asFloat();
        }

        JsonValue minOffset = json.get("minOffset");
        if (minOffset != null) {
            this.minOffset = minOffset.asFloat();
        }

        JsonValue maxOffset = json.get("maxOffset");
        if (maxOffset != null) {
            this.maxOffset = maxOffset.asFloat();
        }

        JsonValue minTime = json.get("minTime");
        if (minTime != null) {
            this.minTime = minTime.asFloat();
        }

        JsonValue maxTime = json.get("maxTime");
        if (maxTime != null) {
            this.maxTime = maxTime.asFloat();
        }
    }
}
