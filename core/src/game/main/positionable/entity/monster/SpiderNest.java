package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.SpriteSheet;
import game.main.Game;

public class SpiderNest extends Monster {
    public float startSpawnTime;
    public float spawnTime;
    public float spawnSpiderTime;

    public SpriteSheet spriteSheet;
    public float scaleX;

    public SpiderNest() {
        hitbox.set(0, 0, 8, 4);

        isPushable = false;

        maxHealth = 11;
        maxHealthPerLevel = 3;
        defense = 2;
        defensePerLevel = 1;
        experience = 5;
        experiencePerLevel = 2;

        receiveKnockback = false;

        spriteSheet = new SpriteSheet("spider_nest");
        scaleX = MathUtils.randomBoolean(.5f) ? 1 : -1;
    }

    @Override
    public void update() {
        super.update();

        startSpawnTime -= Game.getDelta();

        if (startSpawnTime < 0) {
            startSpawnTime = MathUtils.random(5f, 10f);

            spawnTime = 1f;
        }

        if (spawnTime > 0) {
            spawnTime -= Game.getDelta();

            spawnSpiderTime -= Game.getDelta();

            if (spawnSpiderTime < 0) {
                spawnSpiderTime = .1f;

                Spider s = new Spider();
                s.x = x;
                s.y = y;
                map.entities.addEntity(s);
            }
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        TextureRegion t = spriteSheet.grab(0, 0);
        batch.draw(t, x, y, t.getRegionWidth() * .5f, t.getRegionHeight() * .5f,
                t.getRegionWidth(), t.getRegionHeight(), scaleX, 1, 0);
    }

    @Override
    public boolean isAttacking() {
        return false;
    }
}
