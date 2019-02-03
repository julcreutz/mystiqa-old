package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.resource.SpriteSheet;
import game.main.Game;

public class GiantSpiderNest extends Monster {
    public float startSpawnTime;
    public float spawnTime;
    public float spawnSpiderTime;

    public float spawnFireSpiderTime;

    public SpriteSheet spriteSheet;
    public float scaleX;

    public GiantSpiderNest() {
        hitbox.set(1, 6, 14, 8);

        isPushable = false;

        maxHealth = 45;
        maxHealthPerLevel = 9;
        defense = 4;
        defensePerLevel = 2;
        experience = 16;
        experiencePerLevel = 6;
        fireResistance = -.5f;

        receiveKnockback = false;

        spriteSheet = new SpriteSheet("giant_spider_nest");
        scaleX = MathUtils.randomBoolean(.5f) ? 1 : -1;
    }

    @Override
    public void update() {
        super.update();

        if (new Vector2(map.player.x, map.player.y).sub(x, y).len() < 24) {
            startSpawnTime -= Game.getDelta();

            if (startSpawnTime < 0) {
                startSpawnTime = MathUtils.random(5f, 8f);

                spawnTime = 1f;
            }

            if (spawnTime > 0) {
                spawnTime -= Game.getDelta();

                spawnSpiderTime -= Game.getDelta();

                if (spawnSpiderTime < 0) {
                    spawnSpiderTime = .1f;

                    Spider s = new Spider();
                    s.x = x + 4;
                    s.y = y + 4;
                    map.entities.addEntity(s);
                }
            }
        }

        if (isOnFire()) {
            spawnFireSpiderTime -= Game.getDelta();

            if (spawnFireSpiderTime < 0) {
                Spider s = new Spider();
                s.x = x + 4;
                s.y = y + 4;
                s.onFireTime = onFireTime;
                map.entities.addEntity(s);

                spawnFireSpiderTime = .25f;
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
    public void onDeath() {
        super.onDeath();

        for (int i = 0; i < MathUtils.random(5, 10); i++) {
            Spider s = new Spider();
            s.x = x;
            s.y = y;
            s.onFireTime = onFireTime;
            map.player.hit.add(s);
            map.entities.addEntity(s);
        }
    }

    @Override
    public boolean isAttacking() {
        return false;
    }
}
