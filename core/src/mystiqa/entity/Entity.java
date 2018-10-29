package mystiqa.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import mystiqa.main.Game;

public abstract class Entity {
    public float x;
    public float y;

    public float velX;
    public float velY;

    public Rectangle hitbox;
    public Rectangle attackHitbox;
    public Rectangle defendHitbox;

    public Entity() {
        hitbox = new Rectangle();
        attackHitbox = new Rectangle();
        defendHitbox = new Rectangle();
    }

    public void update() {
        if (velX != 0 || velY != 0) {
            onMove();

            x += velX * Game.getDelta();
            y += velY * Game.getDelta();

            velX = 0;
            velY = 0;
        }
    }

    public void render(SpriteBatch batch) {

    }

    public void onMove() {

    }
}
