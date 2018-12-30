package game.main.state.play.map.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.item.equipment.hand.off.OffHand;
import game.main.item.equipment.hand.main.MainHand;
import game.main.item.equipment.armor.BodyArmor;
import game.main.item.equipment.armor.FeetArmor;
import game.main.item.equipment.armor.HeadArmor;
import game.main.stat.Stat;
import game.main.stat.StatType;
import game.main.state.play.Play;
import game.main.state.play.map.Map;
import game.main.state.play.map.tile.Tile;

public class Humanoid extends Entity {
    public SpriteSheet feet;
    public SpriteSheet body;
    public SpriteSheet head;

    public float animSpeed;

    public String[] colors;

    public Hitbox attackHitbox;
    public Hitbox blockHitbox;

    public MainHand mainHand;
    public OffHand offHand;

    public FeetArmor feetArmor;
    public BodyArmor bodyArmor;
    public HeadArmor headArmor;

    public int step;
    public int dir;
    public float time;

    public float lastUsed;

    public Humanoid() {
        hitbox.set(4, 2, 2, 1);
        attackHitbox = new Hitbox();
        blockHitbox = new Hitbox();
    }

    @Override
    public void update(Map map) {
        Vector2 dir = new Vector2();

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            dir.x -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            dir.x += 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            dir.y -= 1;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            dir.y += 1;
        }

        float angle = dir.angle();

        if (dir.x != 0 || dir.y != 0) {
            float speed = statManager.count(StatType.SPEED);

            velX = MathUtils.round(MathUtils.cosDeg(angle) * speed);
            velY = MathUtils.round(MathUtils.sinDeg(angle) * speed);

            if (lastUsed > .01f) {
                switch (MathUtils.floor((angle + 360f) / 45f) % 8) {
                    case 0:
                        this.dir = 0;
                        break;
                    case 2:
                        this.dir = 1;
                        break;
                    case 4:
                        this.dir = 2;
                        break;
                    case 6:
                        this.dir = 3;
                        break;
                }
            }

            Tile t = tileAt(map);

            if (t != null && t.type.forcedDirection != -1) {
                this.dir = t.type.forcedDirection;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.F)) {
            if (mainHand != null) {
                mainHand.use();
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D) && (mainHand == null || !mainHand.isUsing())) {
            if (offHand != null) {
                offHand.use();
            }
        }

        if (mainHand != null) {
            mainHand.update(this);
        }

        if (offHand != null) {
            offHand.update(this);
        }

        step = MathUtils.floor(time * animSpeed) % 4;

        lastUsed += Game.getDelta();

        if ((mainHand != null && mainHand.isUsing()) || (offHand != null && offHand.isUsing())) {
            lastUsed = 0;
        }

        super.update(map);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        float y = this.y + (step % 2 != 0 ? -1 : 0);

        int rightArmIndex = mainHand != null && mainHand.isUsing() ? mainHand.armIndex : step;
        int leftArmIndex = offHand != null && offHand.isUsing() ? offHand.armIndex : step;

        if (mainHand != null && mainHand.renderBehind) {
            mainHand.render(batch, this);
        }

        if (offHand != null && offHand.renderBehind) {
            offHand.render(batch, this);
        }

        switch (dir) {
            case 0:
                // Left foot
                batch.setShader(palette());
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.spriteSheet.sheet[(step + 2) % feet.sheet.length][dir], x, y);
                }

                // Left arm
                batch.setShader(palette());
                batch.draw(body.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, y);
                }

                // Torso
                batch.setShader(palette());
                batch.draw(body.sheet[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[0][dir], x, y);
                }

                // Right foot
                batch.setShader(palette());
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.spriteSheet.sheet[step % feet.sheet.length][dir], x, y);
                }

                // Head
                batch.setShader(palette());
                batch.draw(head.sheet[step % head.sheet.length][dir], x, y);

                if (headArmor != null) {
                    batch.setShader(headArmor.palette);
                    batch.draw(headArmor.spriteSheet.sheet[step % head.sheet.length][dir], x, y);
                }

                // Right arm
                batch.setShader(palette());
                batch.draw(body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);
                }

                break;
            case 2:
                // Right foot
                batch.setShader(palette());
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.spriteSheet.sheet[step % feet.sheet.length][dir], x, y);
                }

                // Right arm
                batch.setShader(palette());
                batch.draw(body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);
                }

                // Torso
                batch.setShader(palette());
                batch.draw(body.sheet[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[0][dir], x, y);
                }

                // Left foot
                batch.setShader(palette());
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.spriteSheet.sheet[(step + 2) % feet.sheet.length][dir], x, y);
                }

                // Head
                batch.setShader(palette());
                batch.draw(head.sheet[step % head.sheet.length][dir], x, y);

                if (headArmor != null) {
                    batch.setShader(headArmor.palette);
                    batch.draw(headArmor.spriteSheet.sheet[step % head.sheet.length][dir], x, y);
                }

                // Left arm
                batch.setShader(palette());
                batch.draw(body.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + leftArmIndex % (body.sheet.length - 1)][dir], x, y);
                }

                break;
            case 1:
                // Left foot
                batch.setShader(palette());
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.spriteSheet.sheet[(step + 2) % feet.sheet.length][dir], x, y);
                }

                // Right foot
                batch.setShader(palette());
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.spriteSheet.sheet[step % feet.sheet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Torso
                batch.setShader(palette());
                batch.draw(body.sheet[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[0][dir], x, y);
                }

                // Left arm
                batch.setShader(palette());
                batch.draw(body.sheet[1 + (leftArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (leftArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);
                }

                // Right arm
                batch.setShader(palette());
                batch.draw(body.sheet[1 + (rightArmIndex) % (body.sheet.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (rightArmIndex) % (body.sheet.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Head
                batch.setShader(palette());
                batch.draw(head.sheet[step % head.sheet.length][dir], x, y);

                if (headArmor != null) {
                    batch.setShader(headArmor.palette);
                    batch.draw(headArmor.spriteSheet.sheet[step % head.sheet.length][dir], x, y);
                }

                break;
            case 3:
                // Left foot
                batch.setShader(palette());
                batch.draw(feet.sheet[(step + 2) % feet.sheet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.spriteSheet.sheet[(step + 2) % feet.sheet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Right foot
                batch.setShader(palette());
                batch.draw(feet.sheet[step % feet.sheet.length][dir], x, y);

                if (feetArmor != null) {
                    batch.setShader(feetArmor.palette);
                    batch.draw(feetArmor.spriteSheet.sheet[step % feet.sheet.length][dir], x, y);
                }

                // Torso
                batch.setShader(palette());
                batch.draw(body.sheet[0][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[0][dir], x, y);
                }

                // Left arm
                batch.setShader(palette());
                batch.draw(body.sheet[1 + (leftArmIndex) % (body.sheet.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (leftArmIndex) % (body.sheet.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);
                }

                // Right arm
                batch.setShader(palette());
                batch.draw(body.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);

                if (bodyArmor != null) {
                    batch.setShader(bodyArmor.palette);
                    batch.draw(bodyArmor.spriteSheet.sheet[1 + (rightArmIndex + 2) % (body.sheet.length - 1)][dir], x, y);
                }

                // Head
                batch.setShader(palette());
                batch.draw(head.sheet[step % head.sheet.length][dir], x, y);

                if (headArmor != null) {
                    batch.setShader(headArmor.palette);
                    batch.draw(headArmor.spriteSheet.sheet[step % head.sheet.length][dir], x, y);
                }

                break;
        }

        if (mainHand != null && !mainHand.renderBehind) {
            mainHand.render(batch, this);
        }

        if (offHand != null && !offHand.renderBehind) {
            offHand.render(batch, this);
        }

        batch.setShader(null);
    }

    @Override
    public void onMove(Map map) {
        super.onMove(map);
        time += Game.getDelta() * (new Vector2(velX, velY).len() / 24f);
    }

    @Override
    public boolean isAttacking() {
        return mainHand != null && mainHand.isUsing();
    }

    @Override
    public boolean isBlocking() {
        return offHand != null && offHand.isUsing();
    }

    @Override
    public String[] colors() {
        return colors;
    }

    @Override
    public Hitbox getAttackHitbox() {
        return attackHitbox;
    }

    @Override
    public Hitbox getBlockHitbox() {
        return blockHitbox;
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("feet")) {
            feet = Game.SPRITE_SHEETS.load(json.getString("feet"));
        }

        if (json.has("body")) {
            body = Game.SPRITE_SHEETS.load(json.getString("body"));
        }

        if (json.has("head")) {
            head = Game.SPRITE_SHEETS.load(json.getString("head"));
        }

        if (json.has("animSpeed")) {
            animSpeed = json.getFloat("animSpeed");
        }

        if (json.has("colors")) {
            colors = json.get("colors").asStringArray();
        }
    }
}
