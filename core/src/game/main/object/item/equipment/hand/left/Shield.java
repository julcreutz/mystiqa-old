package game.main.object.item.equipment.hand.left;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.reference.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.stat.Stat;
import game.main.object.entity.Humanoid;

public class Shield extends LeftHand {
    public SpriteSheet spriteSheet;
    public TextureRegion image;

    public float x;
    public float y;

    public int armIndex;
    public boolean renderBehind;

    public boolean blocking;

    public Stat slowdown;

    public Shield() {
        slowdown = new Stat(Stat.Type.SPEED, 0, -.5f);
    }

    @Override
    public void update(Humanoid h) {
        super.update(h);

        int dir;

        if (!h.isAttacking()) {
            dir = h.dir;

            armIndex = 3;

            switch (dir) {
                case 0:
                    x = h.x + 7;
                    y = h.y + 0;
                    renderBehind = true;
                    h.blockHitbox.set(1, 8, 7, 0);
                    break;
                case 2:
                    x = h.x - 7;
                    y = h.y + 0;
                    renderBehind = false;
                    h.blockHitbox.set(1, 8, 0, 0);
                    break;
                case 1:
                    x = h.x - 2;
                    y = h.y + 1;
                    renderBehind = true;
                    h.blockHitbox.set(8, 4, 0, 4);
                    break;
                case 3:
                    x = h.x + 2;
                    y = h.y - 2;
                    renderBehind = false;
                    h.blockHitbox.set(8, 1, 0, 0);
                    break;
            }

            blocking = true;
        } else {
            dir = (h.dir + 1) % 4;

            armIndex = 1;

            switch (dir) {
                case 0:
                    x = h.x + 7;
                    y = h.y + 0;
                    renderBehind = false;
                    break;
                case 2:
                    x = h.x - 7;
                    y = h.y;
                    renderBehind = false;
                    break;
                case 1:
                    x = h.x;
                    y = h.y + 1;
                    renderBehind = true;
                    break;
                case 3:
                    x = h.x;
                    y = h.y - 2;
                    renderBehind = false;
                    break;
            }

            blocking = false;
        }

        image = spriteSheet.grab(0, dir);

        if (h.step % 2 != 0) {
            y--;
        }
    }

    @Override
    public void render(SpriteBatch batch, Humanoid h) {
        super.render(batch, h);

        ShaderProgram lastShader = batch.getShader();
        if (h.blockTime > 0) {
            batch.setShader(Game.SHADERS.load("HitFlash").shader);
        }
        batch.draw(image, x, y);
        batch.setShader(lastShader);
    }

    @Override
    public int getArmIndex() {
        return armIndex;
    }

    @Override
    public boolean renderBehind() {
        return renderBehind;
    }

    @Override
    public boolean isBlocking() {
        return blocking;
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        JsonValue spriteSheet = json.get("spriteSheet");
        if (spriteSheet != null) {
            this.spriteSheet = Game.SPRITE_SHEETS.load(spriteSheet.asString());
        }
    }
}
