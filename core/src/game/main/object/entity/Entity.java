package game.main.object.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.main.Game;
import game.main.object.GameObject;
import game.main.object.entity.particle.Particle;
import game.main.object.item.Item;
import game.main.stat.Stat;
import game.main.stat.StatCounter;
import game.main.stat.StatManager;
import game.main.state.play.map.Map;
import game.main.object.entity.event.*;
import game.main.object.tile.Tile;

public abstract class Entity extends GameObject implements StatCounter {
    /**
     * Describes a rectangle offset by specified x and y values multiplier to
     * a given entity. Uses {@link Rectangle} class to represent the rectangle itself.
     *
     * @see Rectangle
     */
    public class Hitbox {
        /** Holds reference of entity this applies to. */
        public Entity e;

        /** The rectangle itself. */
        public Rectangle rect;

        /** Value the rectangle is offset by on x axis multiplier to an entity. */
        public float offsetX;

        /** Value of the rectangle is offset by on y axis multiplier to an entity. */
        public float offsetY;

        /**
         * Constructs hitbox initializing rectangle to prevent
         * potential {@link java.lang.NullPointerException}.
         */
        public Hitbox(Entity e) {
            this.e = e;
            rect = new Rectangle();
        }

        /**
         * Positions rectangle multiplier to entity using offsets and two additional offset values.
         *
         * @param offsetX additional x offset
         * @param offsetY additional y offset
         */
        public void position(float offsetX, float offsetY) {
            rect.setPosition(e.x + this.offsetX + offsetX, e.y + this.offsetY + offsetY);
        }

        /** Positions rectangle multiplier to entity using offsets. */
        public void position() {
            position(0, 0);
        }

        /**
         * Checks whether specified rectangle overlaps.
         * Only works if both rectangle's width and height are greater than zero.
         *
         * @param rect rectangle to check overlap
         * @return whether rectangle overlaps
         */
        public boolean overlaps(Rectangle rect) {
            return getWidth() > 0 && getHeight() > 0 && rect.width > 0 && rect.height > 0 && this.rect.overlaps(rect);
        }

        /**
         * Convenience method to check whether specified hitbox overlaps.
         * See {@link #overlaps(Rectangle)} for more details.
         *
         * @param hitbox hitbox to check overlap
         * @return whether hitbox overlaps
         */
        public boolean overlaps(Hitbox hitbox) {
            return overlaps(hitbox.rect);
        }

        /**
         * Convenience method to check whether specified entity's hitbox overlaps.
         * See {@link #overlaps(Rectangle)} for more details.
         *
         * @param e entity to check overlap
         * @return whether hitbox overlaps
         */
        public boolean overlaps(Entity e) {
            return overlaps(e.hitbox);
        }

        /** @return x position of rectangle */
        public float getX() {
            return rect.x;
        }

        /** @return y position of rectangle */
        public float getY() {
            return rect.y;
        }

        /** @return width of rectangle */
        public float getWidth() {
            return rect.width;
        }

        /** @return height of rectangle */
        public float getHeight() {
            return rect.height;
        }

        /** @return x center position of rectangle */
        public float getCenterX() {
            return getX() + getWidth() * .5f;
        }

        /** @return y center position of rectangle */
        public float getCenterY() {
            return getY() + getHeight() * .5f;
        }

        /**
         * Convenience method to initialize all important values at once.
         *
         * @param width width of rectangle
         * @param height height of rectangle
         * @param offsetX x offset of rectangle multiplier to entity
         * @param offsetY y offset of rectangle multiplier to entity
         */
        public void set(float width, float height, float offsetX, float offsetY) {
            rect.setSize(width, height);
            this.offsetX = offsetX;
            this.offsetY = offsetY;
        }

        public void render(SpriteBatch batch) {
            batch.draw(Game.SPRITE_SHEETS.load("GuiLayer").grab(0, 0), getX(), getY(), getWidth(), getHeight());
        }
    }

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

    public boolean updated;

    public StatManager stats;

    public Array<Entity> hit;

    public float hitTime;
    public float hitSpeed;
    public float hitAngle;

    public float health;

    public boolean isMonster;

    public boolean onTeleport;

    public Tile.Overlay overlay;

    public Array<Item> inventory;

    public Entity attachTo;

    public float onFireTime;
    public float onFireParticleTime;

    public Entity() {
        hitbox = new Hitbox(this);
        stats = new StatManager();
        hit = new Array<Entity>();

        inventory = new Array<Item>();
        inventory = new Array<Item>();
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
                        if (!e.isHit() && e.isOnGround() && isHostile(e) && e.isVulnerable()) {
                            boolean contains = hit.contains(e, true);

                            if (isBlockable() && e.isBlocking() && attackHitbox.overlaps(e.getBlockHitbox())) {
                                if (!contains) {
                                    map.screenShake += .5f;

                                    hit.add(e);

                                    e.onBlock(this);
                                    onIsBlocked(e);
                                }
                            } else {
                                if (attackHitbox.overlaps(e)) {
                                    if (!contains) {
                                        e.health -= MathUtils.clamp(count(Stat.Type.PHYSICAL_DAMAGE)
                                                - e.count(Stat.Type.PHYSICAL_DEFENSE), 1, Float.MAX_VALUE);

                                        if (!e.isOnFire()) {
                                            e.onFireTime += MathUtils.clamp((count(Stat.Type.FIRE_DAMAGE)
                                                    - e.count(Stat.Type.FIRE_DEFENSE)), 0, Float.MAX_VALUE);
                                        }

                                        e.hitTime = .1f;

                                        e.hitAngle = new Vector2(e.x, e.y).sub(x, y).angle();
                                        e.hitSpeed = e.isDead() ? 96f : 48f;

                                        map.screenShake += e.isDead() ? 2f : 1f;

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

        if (isPushing() && isOnGround()) {
            for (Entity e : map.entities.entities) {
                if (e != this && e.isPushable() && hitbox.overlaps(e) && e.isOnGround()) {
                    float a = new Vector2(e.x, e.y).sub(x, y).angle();

                    e.velX += MathUtils.cosDeg(a) * 16f;
                    e.velY += MathUtils.sinDeg(a) * 16f;
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

        if (collidesWithSolidTiles()) {
            for (int x = map.x0; x < map.x1; x++) {
                for (int y = map.y0; y < map.y1; y++) {
                    if (map.tiles.inBounds(x, y)) {
                        Rectangle solidTile = map.tiles.solidTiles[x][y];

                        if (solidTile != null && hitbox.overlaps(solidTile)) {
                            if (velX > 0) {
                                this.x = solidTile.x - hitbox.getWidth() - hitbox.offsetX;
                            } else if (velX < 0) {
                                this.x = solidTile.x + solidTile.width - hitbox.offsetX;
                            }

                            velX = 0;
                        }
                    }
                }
            }
        }

        if (collidesWithSolidEntities()) {
            for (Entity e : map.entities.entities) {
                if (e != this && e.isSolid() && e.hitbox.overlaps(this)) {
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

        if (collidesWithSolidTiles()) {
            for (int x = map.x0; x < map.x1; x++) {
                for (int y = map.y0; y < map.y1; y++) {
                    if (map.tiles.inBounds(x, y)) {
                        Rectangle solidTile = map.tiles.solidTiles[x][y];

                        if (solidTile != null && hitbox.overlaps(solidTile)) {
                            if (velY > 0) {
                                this.y = solidTile.y - hitbox.getHeight() - hitbox.offsetY;
                            } else if (velY < 0) {
                                this.y = solidTile.y + solidTile.height - hitbox.offsetY;
                            }

                            velY = 0;
                        }
                    }
                }
            }
        }

        if (collidesWithSolidEntities()) {
            for (Entity e : map.entities.entities) {
                if (e != this && e.isSolid() && e.hitbox.overlaps(this)) {
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

            health -= 1 * Game.getDelta();

            onFireParticleTime -= Game.getDelta();

            if (onFireParticleTime < 0) {
                onFireParticleTime = MathUtils.random(.05f, .1f);

                Particle p = (Particle) Game.ENTITIES.load("Flame");

                p.x = x + MathUtils.random(-2, 2);
                p.y = y + MathUtils.random(-2, 2);

                map.entities.addEntity(p);
            }
        }
    }

    public void preRender(SpriteBatch batch) {
        if (isOnFire()) {
            batch.setShader(Game.SHADERS.load("Burning").shader);
        }

        if (isHit()) {
            batch.setShader(Game.SHADERS.load("HitFlash").shader);
        }
    }

    public void render(SpriteBatch batch) {

    }

    public void postRender(SpriteBatch batch) {
        batch.setShader(null);

        if (overlay != null) {
            overlay.render(batch, this);
        }
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

    /**
     * Returns whether entity is dead. If max health is zero, false is always returned.
     *
     * @return whether entity is dead
     */
    public boolean isDead() {
        return count(Stat.Type.HEALTH) > 0 && health <= 0;
    }

    /** @return whether entity is standing on ground */
    public boolean isOnGround() {
        return true;
    }

    /** @return whether on fire, meaning burning */
    public boolean isOnFire() {
        return onFireTime > 0;
    }

    /** @return correctly positioned hitbox */
    public Hitbox getHitbox() {
        hitbox.position();
        return hitbox;
    }

    /** @return correctly positioned attack hitbox */
    public Hitbox getAttackHitbox() {
        return null;
    }

    /** @return correctly positioned block hitbox */
    public Hitbox getBlockHitbox() {
        return null;
    }

    public boolean isHostile(Entity e) {
        return isMonster != e.isMonster;
    }

    public Tile tileAt() {
        int x = MathUtils.floor((hitbox.getCenterX()) / 8f);
        int y = MathUtils.floor(hitbox.getY() / 8f);

        if (map.tiles.inBounds(x, y)) {
            return map.tiles.at(x, y, 0);
        }

        return null;
    }

    public float getHealthPercentage() {
        return health / count(Stat.Type.HEALTH);
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
        health = count(Stat.Type.HEALTH);
    }

    public void onCollision(Entity e) {
        sendEvent(new CollisionEvent(this, e));
    }

    public void onHit(Entity by) {
        sendEvent(new HitEvent(this, by));

        Particle p = (Particle) Game.ENTITIES.load("Cut");

        p.x = x;
        p.y = y;

        p.attachTo = this;

        map.entities.addEntity(p);
    }

    public void onHasHit(Entity hit) {

    }

    public void onBlock(Entity blocked) {
        sendEvent(new BlockEvent(this, blocked));
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
            Particle p = (Particle) Game.ENTITIES.load("Smoke");

            p.x = x;
            p.y = y;

            map.entities.addEntity(p);
        }
    }

    public void onMove() {
        sendEvent(new MoveEvent(this));
    }

    /** @return whether entity collides with solid tiles */
    public boolean collidesWithSolidTiles() {
        return true;
    }

    public boolean collidesWithSolidEntities() {
        return true;
    }

    /** @return whether entity is able to push other entities away */
    public boolean isPushing() {
        return true;
    }

    /** @return whether entity is able to be pushed away by other entities. */
    public boolean isPushable() {
        return true;
    }

    /**
     * Returns whether entity is solid. Being solid means other entities pushed out similarly to how solid tiles
     * function.
     *
     * @return whether entity is solid
     */
    public boolean isSolid() {
        return false;
    }

    /** @return whether entity is able to be attacked by other entities */
    public boolean isVulnerable() {
        return true;
    }

    /** @return whether attack is able to be blocked by other entities */
    public boolean isBlockable() {
        return true;
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

    @Override
    public float count(Stat.Type type) {
        return stats.count(type);
    }

    @Override
    public StatManager getStats() {
        return stats;
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue stats = json.get("stats");
        if (stats != null) {
            this.stats.deserialize(stats);
        }

        JsonValue isMonster = json.get("isMonster");
        if (isMonster != null) {
            this.isMonster = isMonster.asBoolean();
        }
    }
}
