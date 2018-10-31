package mystiqa.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import mystiqa.entity.humanoid.Humanoid;
import mystiqa.main.screen.PlayScreen;

public class Player extends Humanoid {
    @Override
    public void update(PlayScreen play) {
        Vector2 dir = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dir.x -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dir.x += 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dir.y -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dir.y += 1;
        }

        if (dir.x != 0 || dir.y != 0) {
            float a = dir.angle();
            float speed = 48;

            velX += MathUtils.cosDeg(a) * speed;
            velY += MathUtils.sinDeg(a) * speed;
        }

        if (rightHand != null && Gdx.input.isKeyPressed(Input.Keys.F)) {
            rightHand.use(this);
        }

        if (leftHand != null && Gdx.input.isKeyPressed(Input.Keys.D)) {
            leftHand.use(this);
        }

        super.update(play);
    }
}
