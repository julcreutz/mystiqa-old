package mystiqa.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import mystiqa.Hitbox;
import mystiqa.main.Game;
import mystiqa.main.screen.PlayScreen;

public abstract class Entity {
    public float x;
    public float y;

    public float velX;
    public float velY;

    public Hitbox hitbox;
    public Hitbox attackHitbox;
    public Hitbox defendHitbox;

    public Entity() {
        hitbox = new Hitbox();
        attackHitbox = new Hitbox();
        defendHitbox = new Hitbox();
    }

    public void update(PlayScreen play) {
        // Horizontal collision detection
        hitbox.update(this, velX * Game.getDelta(), 0);

        for (Entity e : play.entities) {
            if (this != e) {
                if (hitbox.hitbox.overlaps(e.hitbox.hitbox)) {
                    if (velX > 0) {
                        x = e.hitbox.hitbox.x - hitbox.hitbox.width - hitbox.x;
                    } else if (velX < 0) {
                        x = e.hitbox.hitbox.x + e.hitbox.hitbox.width - hitbox.x;
                    }

                    velX = 0;
                    break;
                }
            }
        }

        // Vertical collision detection
        hitbox.update(this, 0, velY * Game.getDelta());

        for (Entity e : play.entities) {
            if (this != e) {
                if (hitbox.hitbox.overlaps(e.hitbox.hitbox)) {
                    if (velY > 0) {
                        y = e.hitbox.hitbox.y - hitbox.hitbox.height - hitbox.y;
                    } else if (velY < 0) {
                        y = e.hitbox.hitbox.y + e.hitbox.hitbox.height - hitbox.y;
                    }

                    velY = 0;
                    break;
                }
            }
        }

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

    public void onAdded() {

    }
}
