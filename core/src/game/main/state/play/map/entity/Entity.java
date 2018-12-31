package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
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
import game.main.state.play.map.tile.Tile;

public class Entity implements Serializable {
    public enum Alignment {
        GOOD,
        EVIL;

        public boolean isHostile(Alignment a) {
            return a != this;
        }
    }

    public float x;
    public float y;

    public float velX;
    public float velY;

    public Hitbox hitbox;

    public boolean updated;

    public StatManager statManager;

    public Array<Entity> hit;

    public float hitTime;
    public float hitSpeed;
    public float hitAngle;

    public float health;

    public Alignment alignment;

    public boolean onTeleport;

    public Entity() {
        hitbox = new Hitbox(this);
        statManager = new StatManager();
        hit = new Array<Entity>();
    }

    public void update(Map map) {
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

                                        e.health -= statManager.count(Stat.Type.PHYSICAL_DAMAGE);

                                        e.onHit(map);

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
            hitTime -= Game.delta();

            velX += MathUtils.cosDeg(hitAngle) * hitSpeed;
            velY += MathUtils.sinDeg(hitAngle) * hitSpeed;

            if (hitTime < 0) {
                hitTime = 0;
                hitSpeed = 0;
            }
        } else {
            if (isDead()) {
                onDeath(map);
                map.entities.entities.removeValue(this, true);
            }
        }

        if (isOnGround()) {
            for (Entity e : map.entities.entities) {
                if (e != this && hitbox.overlaps(e) && e.isOnGround()) {
                    float a = new Vector2(e.x, e.y).sub(x, y).angle();

                    velX += MathUtils.cosDeg(a + 180) * 16f;
                    velY += MathUtils.sinDeg(a + 180) * 16f;
                }
            }
        }

        Tile t = tileAt(map);
        float moveSpeed = 1;

        if (t != null) {
            moveSpeed = t.type.moveSpeed;
        }

        velX *= moveSpeed;
        velY *= moveSpeed;

        hitbox.position(velX * Game.delta(), 0);

        for (int x = 0; x < map.tiles.width(); x++) {
            for (int y = 0; y < map.tiles.height(); y++) {
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

        x += velX * Game.delta();

        hitbox.position(0, velY * Game.delta());

        for (int x = 0; x < map.tiles.width(); x++) {
            for (int y = 0; y < map.tiles.height(); y++) {
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

        y += velY * Game.delta();

        if (velX != 0 || velY != 0) {
            onMove(map);
        }

        velX = 0;
        velY = 0;
    }

    public void render(SpriteBatch batch) {
        batch.setShader(palette());
    }

    public void onAdded(Map map) {
        hitbox.position();
        health = statManager.count(Stat.Type.HEALTH);
    }

    public void onMove(Map map) {

    }

    public void onHit(Map map) {

    }

    public void onDeath(Map map) {

    }

    public boolean isAttacking() {
        return false;
    }

    public boolean isBlocking() {
        return false;
    }

    public boolean isHit() {
        return hitTime > 0;
    }

    public boolean isDead() {
        return health <= 0;
    }

    public boolean isOnGround() {
        return true;
    }

    public String[] colors() {
        return null;
    }

    public String[] reverseColors(String[] colors) {
        String[] newColors = new String[colors.length];

        System.arraycopy(colors, 0, newColors, 0, colors.length);

        for (int i = 0; i < newColors.length / 2; i++) {
            int j = newColors.length - 1 - i;

            String color = newColors[j];
            newColors[j] = newColors[i];
            newColors[i] = color;
        }

        return newColors;
    }

    public String[] reverseColors() {
        return reverseColors(colors());
    }

    public ShaderProgram palette() {
        return Game.PALETTES.load(isHit() ? reverseColors() : colors());
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

    public Tile tileAt(Map map) {
        int x = MathUtils.floor((hitbox.centerX()) / 8f);
        int y = MathUtils.floor(hitbox.y() / 8f);

        if (map.tiles.inBounds(x, y)) {
            return map.tiles.tileAt(x, y, 0);
        }

        return null;
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("stats")) {
            statManager.deserialize(json.get("stats"));
        }

        if (json.has("alignment")) {
            alignment = Alignment.valueOf(json.getString("alignment"));
        }
    }
}
