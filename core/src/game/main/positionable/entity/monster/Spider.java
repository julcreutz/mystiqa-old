package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.resource.SpriteSheet;
import game.main.Game;
import game.main.positionable.entity.Entity;
import game.main.positionable.tile.Tile;

public class Spider extends Monster {
    public float idleTime;
    public float idleAngle;
    public float idleSpeed;

    public SpriteSheet spriteSheet;
    public float animTime;
    public float scaleX;

    public Spider() {
        hitbox.set(2, 0, 4, 3);

        maxHealth = 3;
        maxHealthPerLevel = .5f;
        minDamage = 1;
        maxDamage = 1;
        damagePerLevel = .5f;
        defense = 1;
        defensePerLevel = .5f;

        spriteSheet = new SpriteSheet("spider", 2, 1);
        scaleX = 1;
    }

    @Override
    public void update() {
        super.update();

        idleTime -= Game.getDelta();

        if (idleTime < 0) {
            idleTime = MathUtils.random(.25f, 1f);

            idleAngle = MathUtils.random(360f);
            idleSpeed = MathUtils.random(32f, 48f);
        }

        velX += MathUtils.cosDeg(idleAngle) * idleSpeed;
        velY += MathUtils.sinDeg(idleAngle) * idleSpeed;

        animTime += 20f * Game.getDelta() * idleSpeed / 40f;

        if (velX > 0) {
            scaleX = 1;
        } else if (velX < 0) {
            scaleX = -1;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        TextureRegion t = spriteSheet.grab(MathUtils.floor(animTime) % spriteSheet.getColumns(), 0);
        batch.draw(t, x, y, t.getRegionWidth() * .5f, t.getRegionHeight() * .5f,
                t.getRegionWidth(), t.getRegionHeight(), scaleX, 1, 0);
    }

    @Override
    public void onSolidTileCollision(Tile t) {
        super.onSolidTileCollision(t);

        idleTime = 0;
    }

    @Override
    public void onIsBlocked(Entity blocker) {
        super.onIsBlocked(blocker);

        idleTime = 0;
    }
}
