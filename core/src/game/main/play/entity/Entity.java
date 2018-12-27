package game.main.play.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.palette.PaletteShaderLoader;
import game.main.Game;
import game.main.play.Play;
import game.main.stat.StatType;
import game.main.stat.Stats;

public class Entity {
    public float x;
    public float y;

    public float velX;
    public float velY;

    public Hitbox hitbox;

    public boolean updated;

    public Stats stats;

    public Array<Entity> hit;

    public float hitTime;
    public float hitSpeed;
    public float hitAngle;

    public float health;

    public Entity() {
        hitbox = new Hitbox();
        stats = new Stats();
        hit = new Array<>();
    }

    public void update(Play play) {
        Hitbox attackHitbox = getAttackHitbox();
        Hitbox blockHitbox = getBlockHitbox();

        if (attackHitbox != null) {
            getAttackHitbox().position(this);

            if (isAttacking() && isOnGround()) {
                for (Entity e : play.entities) {
                    if (e != this) {
                        if (!e.isHit() && e.isOnGround()) {
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

                                        e.health -= stats.count(StatType.PHYSICAL_DAMAGE);

                                        e.onHit(play);

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
            blockHitbox.position(this);
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
                onDeath(play);
                play.entities.removeValue(this, true);
            }
        }

        if (isOnGround()) {
            for (Entity e : play.entities) {
                if (e != this && hitbox.overlaps(e) && e.isOnGround()) {
                    float a = new Vector2(e.x, e.y).sub(x, y).angle();

                    velX += MathUtils.cosDeg(a + 180) * 16f;
                    velY += MathUtils.sinDeg(a + 180) * 16f;
                }
            }
        }

        hitbox.position(this, velX * Game.delta(), 0);

        for (int x = 0; x < play.solidTiles.length; x++) {
            for (int y = 0; y < play.solidTiles[0].length; y++) {
                Rectangle solidTile = play.solidTiles[x][y];

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

        hitbox.position(this, 0, velY * Game.delta());

        for (int x = 0; x < play.solidTiles.length; x++) {
            for (int y = 0; y < play.solidTiles[0].length; y++) {
                Rectangle solidTile = play.solidTiles[x][y];

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
            onMove(play);
        }

        velX = 0;
        velY = 0;
    }

    public void render(SpriteBatch batch) {
        batch.setShader(palette());
    }

    public void onAdded(Play play) {
        health = stats.count(StatType.MAX_HEALTH);
    }

    public void onMove(Play play) {

    }

    public void onHit(Play play) {

    }

    public void onDeath(Play play) {

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
        return PaletteShaderLoader.load(isHit() ? reverseColors() : colors());
    }

    public Hitbox getAttackHitbox() {
        return null;
    }

    public Hitbox getBlockHitbox() {
        return null;
    }

    public void deserialize(JsonValue json) {

    }
}
