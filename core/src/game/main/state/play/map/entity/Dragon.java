package game.main.state.play.map.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;

public class Dragon extends Entity {
    public static class Head extends Entity {
        public Dragon body;
        public SpriteSheet spriteSheet;

        public float destX;
        public float destY;

        public Head() {
            hitbox.set(8, 8, 0, 0);
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

            batch.draw(spriteSheet.sheet[0][0], x, y);
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

        @Override
        public boolean isPushing() {
            return false;
        }

        @Override
        public boolean isPushable() {
            return false;
        }

        @Override
        public boolean collidesWithSolidTiles() {
            return false;
        }
    }

    public SpriteSheet body;
    public SpriteSheet neck;
    public SpriteSheet head;

    public int minHeads;
    public int maxHeads;

    public Array<Head> heads;

    public Dragon() {
        heads = new Array<Head>();
    }

    @Override
    public void update() {
        super.update();

        if (heads.size == 0) {
            health = 0;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(body.sheet[0][0], x, y);

        for (Head h : heads) {
            for (int i = 0; i < 5; i++) {
                float p = (float) i / 5f;

                batch.draw(neck.sheet[0][0], MathUtils.lerp(x + 4, h.x, p), MathUtils.lerp(y + 2, h.y, p),
                        4, 4, 8, 8, 1, 1, new Vector2(h.x, h.y).sub(x + 4, y + 2).angle() + 90);
            }
        }
    }

    @Override
    public void onAdded() {
        super.onAdded();

        for (int i = 0; i < minHeads + Game.RANDOM.nextInt(maxHeads - minHeads + 1); i++) {
            final Head h = new Head();

            h.stats.stats.addAll(stats.stats);

            h.isMonster = isMonster;

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

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue body = json.get("body");
        if (body != null) {
            this.body = Game.SPRITE_SHEETS.load(body.asString());
        }

        JsonValue neck = json.get("neck");
        if (neck != null) {
            this.neck = Game.SPRITE_SHEETS.load(neck.asString());
        }

        JsonValue head = json.get("head");
        if (head != null) {
            this.head = Game.SPRITE_SHEETS.load(head.asString());
        }

        JsonValue minHeads = json.get("minHeads");
        if (minHeads != null) {
            this.minHeads = minHeads.asInt();
        }

        JsonValue maxHeads = json.get("maxHeads");
        if (maxHeads != null) {
            this.maxHeads = maxHeads.asInt();
        }
    }
}
