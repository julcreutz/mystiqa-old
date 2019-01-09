package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.stat.Stat;
import game.main.stat.StatManager;
import game.main.state.play.map.Map;
import game.main.state.play.map.tile.TileOverlay;
import game.main.state.play.map.tile.Tile;

public class Entity implements Serializable {
    public enum Alignment {
        GOOD,
        EVIL;

        public boolean isHostile(Alignment a) {
            return a != this;
        }
    }

    /** Map instance the entity is currently in. Always update if map is changed. */
    public Map map;

    /** Current x position in pixels. */
    public float x;
    /** Current y position in pixels. */
    public float y;

    /** Current x velocity in pixels per second. */
    public float velX;
    /** Current y velocity in pixels per second. */
    public float velY;

    /** Represents entity physical dimensions mainly used for collision detection. */
    public Hitbox hitbox;

    public boolean updated;

    public StatManager stats;

    public Array<Entity> hit;

    public float hitTime;
    public float hitSpeed;
    public float hitAngle;

    public float health;

    public Alignment alignment;

    public boolean onTeleport;

    public TileOverlay overlay;

    public Entity() {
        hitbox = new Hitbox(this);
        stats = new StatManager();
        hit = new Array<Entity>();
    }

    public void preUpdate() {

    }

    public void update() {

    }

    public void postUpdate() {
        Hitbox attackHitbox = getAttackHitbox();
        Hitbox blockHitbox = getBlockHitbox();

        if (attackHitbox != null) {
            getAttackHitbox().position();

            if (isAttacking() && isOnGround()) {
                for (Entity e : map.entities.entities) {
                    if (e != this) {
                        if (!e.isHit() && e.isOnGround() && isHostile(e)) {
                            boolean contains = hit.contains(e, true);

                            if (e.isBlocking()) {
                                if (attackHitbox.overlaps(e.getBlockHitbox())) {
                                    if (!contains) {
                                        hit.add(e);
                                    }
                                } else {
                                    if (contains) {
                                        hit.removeValue(e, true);
                                    }
                                }
                            } else {
                                if (attackHitbox.overlaps(e)) {
                                    if (!contains) {
                                        e.hitTime = .1f;

                                        e.hitAngle = new Vector2(e.x, e.y).sub(x, y).angle();
                                        e.hitSpeed = 48f;

                                        e.health -= stats.count(Stat.Type.PHYSICAL_DAMAGE);

                                        e.onHit();

                                        hit.add(e);
                                    }
                                } else {
                                    if (contains) {
                                        hit.removeValue(e, true);
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                hit.clear();
            }
        }

        if (blockHitbox != null) {
            blockHitbox.position();
        }

        if (isHit()) {
            hitTime -= Game.getDelta();

            velX += MathUtils.cosDeg(hitAngle) * hitSpeed;
            velY += MathUtils.sinDeg(hitAngle) * hitSpeed;

            if (hitTime < 0) {
                hitTime = 0;
                hitSpeed = 0;
            }
        } else {
            if (isDead()) {
                onDeath();
                map.entities.entities.removeValue(this, true);
            }
        }

        if (isPushing() && isOnGround()) {
            for (Entity e : map.entities.entities) {
                if (e != this && e.isPushable() && hitbox.overlaps(e) && e.isOnGround()) {
                    float a = new Vector2(e.x, e.y).sub(x, y).angle();

                    velX += MathUtils.cosDeg(a + 180) * 16f;
                    velY += MathUtils.sinDeg(a + 180) * 16f;
                }
            }
        }

        Tile t = tileAt();
        float moveSpeed = 1;

        if (t != null) {
            moveSpeed = t.moveSpeed;
            overlay = t.overlay;
        }

        velX *= moveSpeed;
        velY *= moveSpeed;

        hitbox.position(velX * Game.getDelta(), 0);

        for (int x = map.x0; x < map.x1; x++) {
            for (int y = map.y0; y < map.y1; y++) {
                if (map.tiles.inBounds(x, y)) {
                    Rectangle solidTile = map.tiles.solidTiles[x][y];

                    if (solidTile != null && hitbox.overlaps(solidTile)) {
                        if (velX > 0) {
                            this.x = solidTile.x - hitbox.width() - hitbox.offsetX;
                        } else if (velX < 0) {
                            this.x = solidTile.x + solidTile.width - hitbox.offsetX;
                        }

                        velX = 0;
                    }
                }
            }
        }

        x += velX * Game.getDelta();

        hitbox.position(0, velY * Game.getDelta());

        for (int x = map.x0; x < map.x1; x++) {
            for (int y = map.y0; y < map.y1; y++) {
                if (map.tiles.inBounds(x, y)) {
                    Rectangle solidTile = map.tiles.solidTiles[x][y];

                    if (solidTile != null && hitbox.overlaps(solidTile)) {
                        if (velY > 0) {
                            this.y = solidTile.y - hitbox.height() - hitbox.offsetY;
                        } else if (velY < 0) {
                            this.y = solidTile.y + solidTile.height - hitbox.offsetY;
                        }

                        velY = 0;
                    }
                }
            }
        }

        y += velY * Game.getDelta();

        if (velX != 0 || velY != 0) {
            onMove();
        }

        velX = 0;
        velY = 0;
    }

    public void preRender(SpriteBatch batch) {
    }

    public void render(SpriteBatch batch) {

    }

    public void postRender(SpriteBatch batch) {
        if (overlay != null) {
            overlay.render(batch, this);
        }
    }

    /** Called when entity is added to a map. */
    public void onAdded() {
        hitbox.position();
        health = stats.count(Stat.Type.HEALTH);
    }

    /** Called when entity moves. */
    public void onMove() {

    }

    /** Called when entity is hit. */
    public void onHit() {
        map.screenShake += isDead() ? 1f : .5f;
    }

    /** Called when entity dies. */
    public void onDeath() {
    }

    /** @return whether entity is attacking */
    public boolean isAttacking() {
        return false;
    }

    /** @return whether entity is blocking */
    public boolean isBlocking() {
        return false;
    }

    /** @return whether entity is currently hit */
    public boolean isHit() {
        return hitTime > 0;
    }

    /** @return whether entity is dead */
    public boolean isDead() {
        return health <= 0;
    }

    /** @return whether entity is standing on ground */
    public boolean isOnGround() {
        return true;
    }

    public Hitbox getAttackHitbox() {
        return null;
    }

    public Hitbox getBlockHitbox() {
        return null;
    }

    public boolean isHostile(Entity e) {
        return alignment.isHostile(e.alignment);
    }

    public Tile tileAt() {
        int x = MathUtils.floor((hitbox.centerX()) / 8f);
        int y = MathUtils.floor(hitbox.y() / 8f);

        if (map.tiles.inBounds(x, y)) {
            return map.tiles.at(x, y, 0);
        }

        return null;
    }

    public float getHealthPercentage() {
        return health / stats.count(Stat.Type.HEALTH);
    }

    /** @return whether entity is able to push other entities away */
    public boolean isPushing() {
        return true;
    }

    /** @return whether entity is able to be pushed away by other entities. */
    public boolean isPushable() {
        return true;
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue stats = json.get("stats");
        if (stats != null) {
            this.stats.deserialize(stats);
        }

        JsonValue alignment = json.get("alignment");
        if (alignment != null) {
            this.alignment = Alignment.valueOf(alignment.asString());
        }
    }
}
