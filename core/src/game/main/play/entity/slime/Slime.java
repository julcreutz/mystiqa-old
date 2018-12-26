package game.main.play.entity.slime;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import game.main.Game;
import game.main.play.Play;
import game.main.play.entity.Entity;

public class Slime extends Entity {
    public SlimeType type;
    public SlimeState state;

    public float time;

    public float jumpAngle;
    public float jumpSpeed;

    public float z;
    public float velZ;

    public Slime() {
        hitbox.set(8, 8, 0, 0);
        state = SlimeState.RANDOM_MOVEMENT;
    }

    @Override
    public void update(Play play) {
        switch (state) {
            case RANDOM_MOVEMENT:
                time -= Game.delta();

                if (time < 0) {
                    jumpAngle = MathUtils.random(360f);
                    jumpSpeed = MathUtils.random(12f, 24f);

                    velZ = 100;

                    time = MathUtils.random(2f, 3f);
                }

                break;
        }

        velX = MathUtils.cosDeg(jumpAngle) * jumpSpeed;
        velY = MathUtils.sinDeg(jumpAngle) * jumpSpeed;

        z += velZ * Game.delta();
        velZ -= Game.delta() * 512f;

        if (z < 0) {
            z = 0;
            velZ = 0;

            jumpSpeed = 0;
        }

        super.update(play);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        if (z > 0) {
            batch.draw(type.sheet[1][1], x, y);
        }

        batch.draw(type.sheet[0][0], x, y + z);
    }

    @Override
    public String[] colors() {
        return type.colors;
    }
}
