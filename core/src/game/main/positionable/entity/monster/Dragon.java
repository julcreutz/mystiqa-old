package game.main.positionable.entity.monster;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import game.SpriteSheet;
import game.main.Game;
import game.main.positionable.entity.projectile.Fireball;

public class Dragon extends Monster {
    public static class Head extends Monster {
        public Dragon body;
        public SpriteSheet spriteSheet;

        public float destX;
        public float destY;

        public Head() {
            hitbox.set(0, 0, 8, 8);

            isPushable = false;
            isPushing = false;

            collidesWithEntities = false;
            collidesWithTiles = false;
        }

        @Override
        public void update() {
            super.update();

            Vector2 v = new Vector2(destX, destY).sub(x, y);

            if (v.len() < 2) {
                destX = body.x + 8 + MathUtils.random(-16, 16);
                destY = body.y + 2 + MathUtils.random(-16, 16);
            }

            if (destX != x || destY != y) {
                float angle = v.angle();

                velX += MathUtils.cosDeg(angle) * 8f;
                velY += MathUtils.sinDeg(angle) * 8f;
            }
        }

        @Override
        public void render(SpriteBatch batch) {
            super.render(batch);

            batch.draw(spriteSheet.grab(0, 0), x, y);
        }

        @Override
        public void onAdded() {
            super.onAdded();

            destX = x;
            destY = y;
        }

        @Override
        public void onDeath() {
            super.onDeath();

            body.heads.removeValue(this, true);
        }

        @Override
        public float getSortLevel() {
            return Float.MIN_VALUE;
        }
    }

    public enum State {
        IDLE,
        SPEW
    }

    public SpriteSheet body;
    public SpriteSheet neck;
    public SpriteSheet head;

    public int minHeads;
    public int maxHeads;

    public String spew;

    public Array<Head> heads;

    public State state;

    public Head spewing;
    public float spewTime;

    public Dragon() {
        heads = new Array<Head>();

        state = State.IDLE;
    }

    @Override
    public void update() {
        super.update();

        switch (state) {
            case IDLE:
                state = State.SPEW;
                break;
            case SPEW:
                if (spewTime == 0) {
                    spewing = heads.random();
                    spewTime = MathUtils.random(.75f, 1f);

                    Fireball p = new Fireball();

                    p.x = spewing.x;
                    p.y = spewing.y;

                    p.dir = new Vector2(map.player.x, map.player.y).sub(spewing.x, spewing.y).angle();

                    map.entities.addEntity(p);
                }

                spewTime -= Game.getDelta();

                if (spewTime < 0) {
                    spewTime = 0;
                    state = State.IDLE;
                    break;
                }

                break;
        }

        if (heads.size == 0) {
            health = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(body.grab(0, 0), x, y);

        for (Head h : heads) {
            for (int i = 0; i < 5; i++) {
                float p = (float) i / 5f;

                batch.draw(neck.grab(0, 0), MathUtils.lerp(x + 4, h.x, p), MathUtils.lerp(y + 2, h.y, p),
                        4, 4, 8, 8, 1, 1, new Vector2(h.x, h.y).sub(x + 4, y + 2).angle() + 90);
            }
        }
    }

    @Override
    public void onAdded() {
        super.onAdded();

        for (int i = 0; i < minHeads + Game.RANDOM.nextInt(maxHeads - minHeads + 1); i++) {
            final Head h = new Head();

            h.body = this;

            h.spriteSheet = head;

            h.x = x + 4;
            h.y = y + 2;

            heads.add(h);
            map.entities.addEntity(h);
        }
    }

    @Override
    public float getSortLevel() {
        return Float.MAX_VALUE;
    }
}
