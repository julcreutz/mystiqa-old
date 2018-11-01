package mystiqa.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Hitbox;
import mystiqa.entity.stat.Damage;
import mystiqa.entity.stat.MaxHealth;
import mystiqa.entity.stat.StatManager;
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

    public Array<Entity> hit;
    public float hitTime;

    public StatManager stats;

    public int health;

    public Entity() {
        hitbox = new Hitbox();
        attackHitbox = new Hitbox();
        defendHitbox = new Hitbox();

        hit = new Array<Entity>();

        stats = new StatManager();
    }

    public void update(PlayScreen play) {
        attackHitbox.update(this);
        defendHitbox.update(this);

        // Attack collision detection
        for (Entity e : play.entities) {
            if (this != e) {
                if (e.hitTime <= 0) {
                    boolean contains = hit.contains(e, true);

                    if (attackHitbox.overlaps(e.hitbox)) {
                        if (!contains) {
                            hit.add(e);
                            if (attackHitbox.overlaps(e.defendHitbox)) {
                                onDefend();
                            } else {
                                e.hitTime = .1f;

                                e.health -= getDamage();

                                onHit();
                            }
                        }
                    } else if (contains) {
                        hit.removeValue(e, true);
                    }
                }
            }
        }

        if (hitTime > 0) {
            hitTime -= Game.getDelta();
        } else {
            if (health <= 0) {
                onDefend();
                play.entities.removeValue(this, true);
            }
        }

        // Horizontal collision detection
        hitbox.update(this, velX * Game.getDelta(), 0);

        for (Entity e : play.entities) {
            if (this != e) {
                if (hitbox.overlaps(e.hitbox)) {
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
                if (hitbox.overlaps(e.hitbox)) {
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
        health = getMaxHealth();
    }

    public void onHit() {
    }

    public void onDefend() {
    }

    public void onDeath() {

    }

    public int getDamage() {
        return stats.countInteger(Damage.class);
    }

    public int getMaxHealth() {
        return stats.countInteger(MaxHealth.class);
    }

    public void deserialize(JsonValue json) {
        if (json.has("stats")) {
            stats.deserialize(json.get("stats"));
        }
    }
}
