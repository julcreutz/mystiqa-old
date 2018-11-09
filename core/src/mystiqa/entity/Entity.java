package mystiqa.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
    public float z;

    public float velX;
    public float velY;
    public float velZ;

    public Hitbox hitbox;
    public Hitbox attackHitbox;
    public Hitbox defendHitbox;

    public Array<Entity> hit;
    public float hitTime;

    public StatManager stats;

    public int health;

    public Alignment alignment;

    public Entity nearestHostile;

    public boolean attacking;
    public boolean defending;

    public boolean pushing;
    public boolean pushed;

    public boolean gravity;

    public Entity() {
        hitbox = new Hitbox();
        attackHitbox = new Hitbox();
        defendHitbox = new Hitbox();

        hit = new Array<Entity>();

        stats = new StatManager();

        pushing = true;
        pushed = true;

        gravity = true;
    }

    public void update(PlayScreen play) {
        // Find nearest hostile
        if (alignment != null && nearestHostile == null) {
            float nearestDist = Float.MAX_VALUE;

            for (Entity other : play.entities) {
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

        hitbox.update(this, velX * Game.getDelta(), velY * Game.getDelta(), velZ * Game.getDelta());
        attackHitbox.update(this);
        defendHitbox.update(this);

        // Attack collision detection
        if (attacking) {
            for (Entity e : play.entities) {
                if (this != e) {
                    if (e.hitTime <= 0) {
                        boolean contains = hit.contains(e, true);

                        if (attackHitbox.overlaps(e.hitbox)) {
                            if (!contains) {
                                hit.add(e);
                                if (e.defending && attackHitbox.overlaps(e.defendHitbox)) {
                                    e.onDefend();
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
        /*
        if (pushed) {
            for (Entity e : play.entities) {
                if (this != e) {
                    if (e.pushing && hitbox.overlaps(e.hitbox)) {
                        Vector3 v = new Vector3(x, y, z).sub(e.x, e.y, e.z).nor();

                        x += v.x;
                        y += v.y;

                        if (v.z != 0) {
                            z += v.z;
                            velZ = 0;
                        }
                    }
                }
            }
        }*/

        float newX = x;
        float newY = y;
        float newZ = z;

        if (gravity) {
            velZ -= 500 * Game.getDelta();
        }

        hitbox.update(this, velX * Game.getDelta(), 0, 0);

        for (Entity e : play.entities) {
            if (this != e) {
                if (hitbox.overlaps(e.hitbox)) {
                    if (velX > 0) {
                        newX = e.hitbox.x - hitbox.ox - hitbox.w;
                    } else if (velX < 0) {
                        newX = e.hitbox.x + e.hitbox.w - hitbox.ox;
                    }

                    velX = 0;
                }
            }
        }

        hitbox.update(this, 0, velY * Game.getDelta(), 0);

        for (Entity e : play.entities) {
            if (this != e) {
                if (hitbox.overlaps(e.hitbox)) {
                    if (velY > 0) {
                        newY = e.hitbox.y - hitbox.oy - hitbox.h;
                    } else if (velY < 0) {
                        newY = e.hitbox.y + e.hitbox.h - hitbox.oy;
                    }

                    velY = 0;
                }
            }
        }

        hitbox.update(this, 0, 0, velZ * Game.getDelta());

        for (Entity e : play.entities) {
            if (this != e) {
                if (hitbox.overlaps(e.hitbox)) {
                    if (velZ > 0) {
                        newZ = e.hitbox.z - hitbox.oz - hitbox.d;
                    } else if (velZ < 0) {
                        newZ = e.hitbox.z + e.hitbox.d - hitbox.oz;
                    }

                    velZ = 0;
                }
            }
        }

        x = newX;
        y = newY;
        z = newZ;

        x += velX * Game.getDelta();
        y += velY * Game.getDelta();
        z += velZ * Game.getDelta();

        if (velX != 0 || velY != 0) {
            onMove();

            velX = 0;
            velY = 0;
        }

        hitbox.update(this);
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
        return MathUtils.clamp(stats.countInteger(MaxHealth.class), 1, Integer.MAX_VALUE);
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
}
