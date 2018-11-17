package mystiqa.entity.being;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Hitbox;
import mystiqa.Resources;
import mystiqa.entity.Entity;
import mystiqa.entity.tile.Tile;
import mystiqa.stat.Damage;
import mystiqa.stat.MaxHealth;
import mystiqa.stat.StatManager;
import mystiqa.main.Game;
import mystiqa.main.screen.Play;

public abstract class Being extends Entity {
    public float velX;
    public float velY;
    public float velZ;

    public Hitbox attackHitbox;
    public Hitbox defendHitbox;

    public Array<Being> hit;
    public float hitTime;

    public StatManager stats;

    public int health;

    public Alignment alignment;

    public Being nearestHostile;

    public boolean attacking;
    public boolean defending;

    public boolean pushing;
    public boolean pushed;

    public boolean gravity;
    public boolean collisionDetection;
    public boolean onGround;

    public Being() {
        attackHitbox = new Hitbox();
        defendHitbox = new Hitbox();

        stats = new StatManager();

        hit = new Array<Being>();

        stats = new StatManager();

        pushing = true;
        pushed = true;

        gravity = true;
        collisionDetection = true;
    }

    public void update() {
        // Find nearest hostile
        if (alignment != null && nearestHostile == null) {
            float nearestDist = Float.MAX_VALUE;

            for (Being other : Play.getInstance().beings) {
                if (this != other) {
                    if (other.alignment != null && alignment.isHostile(other.alignment)) {
                        float dist = new Vector2(other.x, other.y).sub(x, y).len();

                        if (dist < nearestDist) {
                            nearestDist = dist;
                            nearestHostile = other;
                        }
                    }
                }
            }
        }

        hitbox.update(this);
        attackHitbox.update(this);
        defendHitbox.update(this);

        // Attack collision detection
        if (attacking) {
            for (Being e : Play.getInstance().beings) {
                if (this != e) {
                    if (e.hitTime <= 0) {
                        boolean contains = hit.contains(e, true);

                        if (attackHitbox.overlaps(e.hitbox)) {
                            if (!contains) {
                                hit.add(e);
                                if (e.defending && attackHitbox.overlaps(e.defendHitbox)) {
                                    e.onDefend();

                                    Play.getInstance().screenShake += 1;
                                } else {
                                    e.hitTime = .1f;

                                    e.health -= getDamage();

                                    e.onHit(this);
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
                onDeath();
                Play.getInstance().beings.removeValue(this, true);
            }
        }

        // Tile collision detection
        float newX = x;
        float newY = y;
        float newZ = z;

        if (collisionDetection) {
            hitbox.update(this, velX * Game.getDelta(), 0, 0);

            boolean collided = false;

            for (Tile t : Play.getInstance().getTiles()) {
                t.hitbox.update(t);

                if (hitbox.overlaps(t.hitbox)) {
                    if (velX > 0) {
                        newX = t.hitbox.x - hitbox.w - hitbox.ox;
                    } else if (velX < 0) {
                        newX = t.hitbox.x + t.hitbox.w - hitbox.ox;
                    }

                    collided = true;
                }
            }

            if (collided) {
                velX = 0;
            }
        }

        x = newX;
        x += velX * Game.getDelta();

        if (collisionDetection) {
            hitbox.update(this, 0, velY * Game.getDelta(), 0);

            boolean collided = false;

            for (Tile t : Play.getInstance().getTiles()) {
                t.hitbox.update(t);

                if (hitbox.overlaps(t.hitbox)) {
                    if (velY > 0) {
                        newY = t.hitbox.y - hitbox.h - hitbox.oy;
                    } else if (velY < 0) {
                        newY = t.hitbox.y + t.hitbox.h - hitbox.oy;
                    }

                    collided = true;
                }
            }

            if (collided) {
                velY = 0;
            }
        }

        y = newY;
        y += velY * Game.getDelta();

        if (gravity) {
            velZ -= 400 * Game.getDelta();
        }

        if (collisionDetection) {
            hitbox.update(this, 0, 0, velZ * Game.getDelta());

            boolean collided = false;

            for (Tile t : Play.getInstance().getTiles()) {
                t.hitbox.update(t);

                if (hitbox.overlaps(t.hitbox)) {
                    if (velZ > 0) {
                        newZ = t.hitbox.z - hitbox.d - hitbox.oz;
                    } else if (velZ < 0) {
                        newZ = t.hitbox.z + t.hitbox.d - hitbox.oz;
                    }

                    collided = true;
                }
            }

            if (collided) {
                velZ = 0;
            }
        }

        z = newZ;
        z += velZ * Game.getDelta();

        if (velZ == 0) {
            if (!onGround) {
                onGround();
                onGround = true;
            }
        } else {
            onGround = false;
        }

        if (velX != 0 || velY != 0 || velZ != 0) {
            velX = MathUtils.round(velX);
            velY = MathUtils.round(velY);

            onMove();

            velX = 0;
            velY = 0;
        }
    }

    public void render(SpriteBatch batch) {
        super.render(batch);

        if (hitTime > 0) {
            batch.setColor(Resources.getInstance().getColor("White"));
        } else {
            batch.setShader(null);
        }
    }

    public void onMove() {

    }

    public void onAdded() {
        super.onAdded();

        health = getMaxHealth();
        hitbox.update(this);
    }

    public void onHit(Being e) {
        Play.getInstance().screenShake += isDead() ? 2 : 1;
        nearestHostile = e;
    }

    public void onDefend() {
    }

    public void onDeath() {
    }

    public void onGround() {

    }

    public int getDamage() {
        return stats.countInteger(Damage.class);
    }

    public int getMaxHealth() {
        return MathUtils.clamp(stats.countInteger(MaxHealth.class), 1, Integer.MAX_VALUE);
    }

    public boolean isDead() {
        return health <= 0;
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("stats")) {
            stats.deserialize(json.get("stats"));
        }
    }
}
