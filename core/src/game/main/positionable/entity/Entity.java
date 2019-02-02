package game.main.positionable.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.SpriteSheet;
import game.main.Game;
import game.main.positionable.Hitbox;
import game.main.positionable.Positionable;
import game.main.positionable.entity.monster.Monster;
import game.main.positionable.entity.particle.Cut;
import game.main.positionable.entity.particle.Flame;
import game.main.positionable.entity.particle.Smoke;
import game.main.item.Item;
import game.main.state.play.map.Map;
import game.main.positionable.entity.event.*;
import game.main.positionable.tile.Tile;

public abstract class Entity implements Positionable {
    /** Holds reference of entity manager. */
    public EntityManager entities;

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

    public float health;
    public float maxHealth;
    public float maxHealthPerLevel;

    public float speed;

    public float minDamage;
    public float maxDamage;
    public float damagePerLevel;
    public float defense;
    public float defensePerLevel;

    public float fire;
    public float fireResistance;

    public boolean blocking;

    public float block;
    public float maxBlock;

    public int level;

    public boolean updated;

    public Array<Entity> hit;

    public float hitTime;
    public float hitSpeed;
    public float hitAngle;

    public boolean onTeleport;

    public Tile.Overlay overlay;

    public Array<Item> inventory;

    public Entity attachTo;

    public float onFireTime;
    public float onFireParticleTime;

    public float stunTime;

    public float hitFlashTime;

    public SpriteSheet star;

    public boolean isFlying;

    public boolean receiveKnockback;

    public float hitShakeMultiplier;

    public boolean collidesWithTiles;
    public boolean collidesWithEntities;

    public boolean isPushing;
    public boolean isPushable;

    public boolean isSolid;

    public boolean isVulnerable;

    public boolean isBlockable;

    public Entity() {
        hitbox = new Hitbox(this);

        hit = new Array<Entity>();

        inventory = new Array<Item>();

        star = new SpriteSheet("star");

        receiveKnockback = true;

        hitShakeMultiplier = 1;

        collidesWithTiles = true;
        collidesWithEntities = true;

        isPushing = true;
        isPushable = true;

        isVulnerable = true;

        isBlockable = true;
    }

    public void preUpdate() {

    }

    public void update() {
    }

    public void postUpdate() {
        if (attachTo != null) {
            x = attachTo.x;
            y = attachTo.y;
        }

        hitbox.position(velX * Game.getDelta(), velY * Game.getDelta());

        for (Entity e : map.entities.entities) {
            if (hitbox.overlaps(e)) {
                onCollision(e);
                e.onCollision(this);
            }
        }

        hitbox.position();

        Hitbox attackHitbox = getAttackHitbox();
        Hitbox blockHitbox = getBlockHitbox();

        if (attackHitbox != null) {
            getAttackHitbox().position();

            if (isAttacking() && isOnGround()) {
                for (Entity e : map.entities.entities) {
                    if (e != this) {
                        if (!e.isHit() && e.isOnGround() && isHostile(e) && e.isVulnerable) {
                            boolean contains = hit.contains(e, true);

                            if (isBlockable && e.blocking && attackHitbox.overlaps(e.getBlockHitbox())) {
                                if (!contains) {
                                    map.shakeScreen(.5f);

                                    hit.add(e);

                                    e.onBlock(this);
                                    onIsBlocked(e);
                                }
                            } else {
                                if (attackHitbox.overlaps(e)) {
                                    if (!contains) {
                                        if (e.health > 0) {
                                            float damage = getDamage();

                                            float dmg = MathUtils.clamp((damage * (1 - getFire())) - e.getDefense(), 1, Float.MAX_VALUE);
                                            e.health -= dmg;

                                            float fireDamage = MathUtils.clamp(damage * getFire(), 0, Float.MAX_VALUE) * (1 - e.getFireResistance());
                                            e.health -= fireDamage;

                                            if (fireDamage > 0 && !e.isOnFire() && Game.RANDOM.nextFloat() < getFire()) {
                                                e.onFireTime = MathUtils.random(1f, 3f);
                                            }
                                        }

                                        e.hitTime = .1f;

                                        if (e.receiveKnockback) {
                                            e.hitAngle = new Vector2(e.x, e.y).sub(x, y).angle();
                                            e.hitSpeed = e.isDead() ? 96f : 48f;
                                        }

                                        map.shakeScreen((e.isDead() ? 2f : 1f) * e.hitShakeMultiplier);

                                        hit.add(e);

                                        e.onHit(this);
                                        onHasHit(e);
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
            }
        }

        if (isPushing && isOnGround()) {
            for (Entity e : map.entities.entities) {
                if (e != this && e.isPushable && hitbox.overlaps(e) && e.isOnGround()) {
                    float a = new Vector2(e.x, e.y).sub(x, y).angle();

                    e.velX += MathUtils.cosDeg(a) * 16f;
                    e.velY += MathUtils.sinDeg(a) * 16f;
                }
            }
        }

        Tile tileAt = tileAt();
        float moveSpeed = getSpeed();

        if (tileAt != null && !isFlying) {
            moveSpeed = tileAt.moveSpeed;
            overlay = tileAt.overlay;
        }

        velX *= moveSpeed;
        velY *= moveSpeed;

        hitbox.position(velX * Game.getDelta(), 0);

        if (collidesWithTiles) {
            for (int x = map.x0; x < map.x1; x++) {
                for (int y = map.y0; y < map.y1; y++) {
                    Tile t = map.tiles.at(x, y);

                    if (t != null && t.hitbox != null && (!isFlying || !t.isOverfliable) && hitbox.overlaps(t.hitbox)) {
                        if (velX > 0) {
                            this.x = t.hitbox.getX() - hitbox.getWidth() - hitbox.offsetX;
                        } else if (velX < 0) {
                            this.x = t.hitbox.getX() + t.hitbox.getWidth() - hitbox.offsetX;
                        }

                        velX = 0;

                        onSolidTileCollision(t);
                    }
                }
            }
        }

        if (collidesWithEntities) {
            for (Entity e : map.entities.entities) {
                if (e != this && e.isSolid && e.hitbox.overlaps(this)) {
                    if (velX > 0) {
                        this.x = e.hitbox.getX() - hitbox.getWidth() - hitbox.offsetX;
                    } else if (velX < 0) {
                        this.x = e.hitbox.getX() + e.hitbox.getWidth() - hitbox.offsetX;
                    }

                    velX = 0;
                }
            }
        }

        x += velX * Game.getDelta();

        hitbox.position(0, velY * Game.getDelta());

        if (collidesWithTiles) {
            for (int x = map.x0; x < map.x1; x++) {
                for (int y = map.y0; y < map.y1; y++) {
                    Tile t = map.tiles.at(x, y);

                    if (t != null && t.hitbox != null && (!isFlying || !t.isOverfliable) && hitbox.overlaps(t.hitbox)) {
                        if (velY > 0) {
                            this.y = t.hitbox.getY() - hitbox.getHeight() - hitbox.offsetY;
                        } else if (velY < 0) {
                            this.y = t.hitbox.getY() + t.hitbox.getHeight() - hitbox.offsetY;
                        }

                        velY = 0;

                        onSolidTileCollision(t);
                    }
                }
            }
        }

        if (collidesWithEntities) {
            for (Entity e : map.entities.entities) {
                if (e != this && e.isSolid && e.hitbox.overlaps(this)) {
                    if (velY > 0) {
                        this.y = e.hitbox.getY() - hitbox.getHeight() - hitbox.offsetY;
                    } else if (velY < 0) {
                        this.y = e.hitbox.getY() + e.hitbox.getHeight() - hitbox.offsetY;
                    }

                    velY = 0;
                }
            }
        }

        y += velY * Game.getDelta();

        if (velX != 0 || velY != 0) {
            onMove();
        }

        velX = 0;
        velY = 0;

        if (isOnFire()) {
            onFireTime -= Game.getDelta();

            if (health > 0) {
                health -= 1 * Game.getDelta();
            } else {
                if (isDead() && health < 0) {
                    health = 0;
                }
            }

            onFireParticleTime -= Game.getDelta();

            if (onFireParticleTime < 0) {
                onFireParticleTime = MathUtils.random(.05f, .1f);

                Flame f = new Flame();

                f.x = x + MathUtils.random(-2, 2);
                f.y = y + MathUtils.random(-2, 2);

                map.entities.addEntity(f);
            }
        }

        if (isStunned()) {
            stunTime -= Game.getDelta();

            if (stunTime < 0) {
                stunTime = 0;
            }
        }

        if (hitFlashTime > 0) {
            hitFlashTime -= Game.getDelta();

            if (hitFlashTime < 0) {
                hitFlashTime = 0;
            }
        }
    }

    public void preRender(SpriteBatch batch) {
        if (isOnFire()) {
            batch.setShader(Game.SHADERS.load("burning"));
        }

        if (isHit() || hitFlashTime > 0) {
            batch.setShader(Game.SHADERS.load("hit_flash"));
        }
    }

    public void render(SpriteBatch batch) {

    }

    public void postRender(SpriteBatch batch) {
        batch.setShader(null);

        if (overlay != null) {
            overlay.render(batch, this);
        }

        if (isStunned()) {
            TextureRegion star = this.star.grab(0, 0);

            for (int i = 0; i < 3; i++) {
                float angle = Game.time * 360f + i * 120f;

                batch.draw(star, hitbox.getCenterX() - star.getRegionWidth() * .5f + MathUtils.cosDeg(angle) * 4f,
                        hitbox.getY() + hitbox.getHeight() + MathUtils.sinDeg(angle) * 2f);
            }
        }
    }

    public float getMaxHealth() {
        return maxHealth + MathUtils.floor(level * maxHealthPerLevel);
    }

    public float getHealthPercentage() {
        return health / getMaxHealth();
    }

    public float getSpeed() {
        return speed;
    }

    public float getDamage() {
        return minDamage + Game.RANDOM.nextInt((int) (maxDamage - minDamage + 1)) + MathUtils.floor(level * damagePerLevel);
    }

    public float getDefense() {
        return defense + MathUtils.floor(level * defensePerLevel);
    }

    public float getFire() {
        return fire + (isOnFire() ? 1 : 0);
    }

    public float getFireResistance() {
        return fireResistance;
    }

    /** @return whether entity is attacking */
    public boolean isAttacking() {
        return !isStunned();
    }

    /** @return whether entity is currently hit */
    public boolean isHit() {
        return hitTime > 0;
    }

    /**
     * Returns whether entity is dead. If max health is zero, false is always returned.
     *
     * @return whether entity is dead
     */
    public boolean isDead() {
        return maxHealth > 0 && health <= 0;
    }

    /** @return whether entity is standing on ground */
    public boolean isOnGround() {
        return true;
    }

    /** @return whether on fire, meaning burning */
    public boolean isOnFire() {
        return onFireTime > 0;
    }

    public boolean isStunned() {
        return stunTime > 0;
    }

    /** @return correctly positioned hitbox */
    public Hitbox getHitbox() {
        hitbox.position();
        return hitbox;
    }

    /** @return correctly positioned attack hitbox */
    public Hitbox getAttackHitbox() {
        return getHitbox();
    }

    /** @return correctly positioned block hitbox */
    public Hitbox getBlockHitbox() {
        return getHitbox();
    }

    public boolean isHostile(Entity e) {
        return e instanceof Monster;
    }

    public Tile tileAt() {
        int x = MathUtils.floor((hitbox.getCenterX()) / 8f);
        int y = MathUtils.floor(hitbox.getY() / 8f);

        if (map.tiles.inBounds(x, y)) {
            return map.tiles.at(x, y);
        }

        return null;
    }

    public void onDisabled() {
        sendEvent(new DisabledEvent(this));

        if (removeOnDisabled()) {
            map.entities.removeEntity(this);
        }
    }

    public void onEnabled() {
        sendEvent(new EnabledEvent(this));
    }

    public void onAdded() {
        sendEvent(new AddedEvent(this));

        hitbox.position();
        health = getMaxHealth();
    }

    public void onCollision(Entity e) {
        sendEvent(new CollisionEvent(this, e));
    }

    public void onHit(Entity by) {
        sendEvent(new HitEvent(this, by));

        Cut c = new Cut(new Vector2(x, y).sub(by.x, by.y).angle() + 90);

        c.x = x;
        c.y = y;

        c.attachTo = this;

        map.entities.addEntity(c);
    }

    public void onHasHit(Entity hit) {

    }

    public void onBlock(Entity blocked) {
        sendEvent(new BlockEvent(this, blocked));

        block -= blocked.getDamage();
        if (block < 0) {
            stunTime = Math.abs(block);
            setBlocking(false);
        }
    }

    public void onIsBlocked(Entity blocker) {

    }

    public void onDeath() {
        sendEvent(new DeathEvent(this));

        map.entities.entities.removeValue(this, true);

        for (Item i : inventory) {
            ItemDrop drop = new ItemDrop(i);

            drop.x = x;
            drop.y = y;

            map.entities.addEntity(drop);
        }

        for (int i = 0; i < MathUtils.random(5, 10); i++) {
            Smoke p = new Smoke();

            p.x = x;
            p.y = y;

            map.entities.addEntity(p);
        }
    }

    public void onMove() {
        sendEvent(new MoveEvent(this));
    }

    public void onSolidTileCollision(Tile t) {

    }

    /**
     * Returns entity sorting value to create perspective. Sorting is done by {@link EntityManager}.
     * See {@link EntityManager#update()} for more details on sorting.
     *
     * To have entity below all other entities, use {@link Float#MAX_VALUE} or other large values.
     * To have entity above all other entities, use {@link Float#MIN_VALUE} or other small values.
     *
     * @return entity sort level
     */
    public float getSortLevel() {
        return hitbox.getY();
    }

    public void sendEvent(EntityEvent e) {
        entities.sendEvent(e);
    }

    public boolean removeOnDisabled() {
        return false;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;

        if (blocking) {
            block = getMaxBlock();
        } else {
            block = 0;
        }
    }

    public float getMaxBlock() {
        return maxBlock;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }
}
