package mystiqa.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Hitbox;
import mystiqa.Resources;
import mystiqa.stat.Damage;
import mystiqa.stat.MaxHealth;
import mystiqa.stat.StatManager;
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

    public Alignment alignment;

    public Entity nearestHostile;

    public Entity() {
        hitbox = new Hitbox();
        attackHitbox = new Hitbox();
        defendHitbox = new Hitbox();

        hit = new Array<Entity>();

        stats = new StatManager();
    }

    public void update(PlayScreen play) {
        // Find nearest hostile
        if (nearestHostile == null) {
            float nearestDist = Float.MAX_VALUE;

            for (Entity other : play.entities) {
                if (this != other) {
                    if (alignment.isHostile(other.alignment)) {
                        float dist = new Vector2(other.x, other.y).sub(x, y).len();

                        if (dist < nearestDist) {
                            nearestDist = dist;
                            nearestHostile = other;
                        }
                    }
                }
            }
        }

        attackHitbox.update(this);
        defendHitbox.update(this);

        // Attack collision detection
        if (isAttacking()) {
            for (Entity e : play.entities) {
                if (this != e) {
                    if (e.hitTime <= 0) {
                        boolean contains = hit.contains(e, true);

                        if (attackHitbox.overlaps(e.hitbox)) {
                            if (!contains) {
                                hit.add(e);
                                if (e.isDefending() && attackHitbox.overlaps(e.defendHitbox)) {
                                    onDefend();
                                } else {
                                    e.hitTime = .1f;

                                    e.health -= getDamage();

                                    e.onHit(play, this);
                                }
                            }
                        } else if (contains) {
                            hit.removeValue(e, true);
                        }
                    }
                }
            }
        } else {
            if (hit.size > 0) {
                hit.clear();
            }
        }

        if (hitTime > 0) {
            hitTime -= Game.getDelta();
        } else {
            if (isDead()) {
                onDeath(play);
                play.entities.removeValue(this, true);
            }
        }

        // Collision detection
        if (isPushed()) {
            hitbox.update(this, velX * Game.getDelta(), velY * Game.getDelta());

            for (Entity e : play.entities) {
                if (this != e) {
                    if (e.isPushing() && hitbox.overlaps(e.hitbox)) {
                        Vector2 v = new Vector2(x, y).sub(e.x, e.y).nor();

                        x += v.x;
                        y += v.y;
                    }
                }
            }
        }

        x += velX * Game.getDelta();
        y += velY * Game.getDelta();

        if (velX != 0 || velY != 0) {
            onMove();

            velX = 0;
            velY = 0;
        }
    }

    public void render(SpriteBatch batch) {
        if (hitTime > 0) {
            batch.setShader(Game.colorToAbsolute(Resources.getColor("White")));
        } else {
            batch.setShader(null);
        }
    }

    public void onMove() {

    }

    public void onAdded() {
        health = getMaxHealth();
        hitbox.update(this);
    }

    public void onHit(PlayScreen play, Entity e) {
        play.screenShake += isDead() ? 2 : 1;
        nearestHostile = e;
    }

    public void onDefend() {
    }

    public void onDeath(PlayScreen play) {
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

        if (json.has("alignment")) {
            alignment = Alignment.valueOf(json.getString("alignment"));
        }
    }

    public boolean isDead() {
        return health <= 0;
    }

    public boolean isAttacking() {
        return false;
    }

    public boolean isDefending() {
        return false;
    }

    public boolean isPushed() {
        return true;
    }

    public boolean isPushing() {
        return true;
    }
}
