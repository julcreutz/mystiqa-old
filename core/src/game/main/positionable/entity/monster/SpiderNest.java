package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import game.resource.SpriteSheet;
import game.main.Game;
import game.main.positionable.entity.Entity;

public class SpiderNest extends Monster {
    public float startSpawnTime;
    public float spawnTime;
    public float spawnSpiderTime;

    public float spawnFireSpiderTime;

    public SpriteSheet spriteSheet;
    public float scaleX;

    public SpiderNest() {
        hitbox.set(0, 4, 8, 4);

        isPushable = false;

        maxHealth = 5;
        fireResistance = -.5f;

        receiveKnockback = false;

        spriteSheet = new SpriteSheet("entities/monsters/spider_nest");
        scaleX = MathUtils.randomBoolean(.5f) ? 1 : -1;
    }

    @Override
    public void update() {
        super.update();

        if (spawnTime > 0) {
            spawnTime -= Game.getDelta();

            spawnSpiderTime -= Game.getDelta();

            if (spawnSpiderTime < 0) {
                spawnSpiderTime = .2f;

                SpiderBaby s = new SpiderBaby();
                s.x = x;
                s.y = y;
                map.player.hit.add(s);
                map.entities.addEntity(s);
            }
        }

        /*
        if (new Vector2(map.player.x, map.player.y).sub(x, y).len() < 24) {
            startSpawnTime -= Game.getDelta();

            if (startSpawnTime < 0) {
                startSpawnTime = MathUtils.random(3f, 6f);

                spawnTime = 1f;
            }

            if (spawnTime > 0) {
                spawnTime -= Game.getDelta();

                spawnSpiderTime -= Game.getDelta();

                if (spawnSpiderTime < 0) {
                    spawnSpiderTime = .2f;

                    Spider s = new Spider();
                    s.x = x;
                    s.y = y;
                    map.entities.addEntity(s);
                }
            }
        }
        */

        if (isOnFire()) {
            spawnFireSpiderTime -= Game.getDelta();

            if (spawnFireSpiderTime < 0) {
                SpiderBaby s = new SpiderBaby();
                s.x = x;
                s.y = y;
                s.onFireTime = onFireTime;
                map.entities.addEntity(s);

                spawnFireSpiderTime = .5f;
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

        for (int i = 0; i < 5; i++) {
            SpiderBaby s = new SpiderBaby();
            s.x = x;
            s.y = y;
            s.onFireTime = onFireTime;
            map.player.hit.add(s);
            map.entities.addEntity(s);
        }
    }

    @Override
    public void onHit(Entity by) {
        super.onHit(by);

        //spawnTime = .2f * 3;
    }

    @Override
    public boolean isAttacking() {
        return false;
    }
}
