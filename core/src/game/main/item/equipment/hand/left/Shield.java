package game.main.item.equipment.hand.left;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import game.SpriteSheet;
import game.main.Game;
import game.main.entity.event.BlockEvent;
import game.main.entity.event.EntityEvent;
import game.main.stat.Stat;
import game.main.entity.Humanoid;

public class Shield extends LeftHand {
    public SpriteSheet spriteSheet;
    public TextureRegion image;

    public float x;
    public float y;

    public int armIndex;
    public boolean renderBehind;

    @Override
    public void update(Humanoid h) {
        super.update(h);

        int dir;

        if (isUsing() && !h.isAttacking()) {
            dir = h.dir;

            h.moveSpeed *= .5f;

            armIndex = 3;

            h.changeDirection = false;

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
        } else {
            dir = (h.dir + 1) % 4;

            armIndex = 0;

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
        }

        image = spriteSheet.grab(0, dir);

        if (h.step % 2 != 0) {
            y--;
        }
    }

    @Override
    public void eventReceived(EntityEvent e) {
        super.eventReceived(e);

        if (e instanceof BlockEvent) {
            if (useTime < .0125f) {
                e.e.map.screenShake += 2;
                ((BlockEvent) e).blocked.hitFlashTime += .1f;
                ((BlockEvent) e).blocked.stunTime += 2f;
            }
        }
    }

    @Override
    public void render(SpriteBatch batch, Humanoid h) {
        super.render(batch, h);

        ShaderProgram lastShader = batch.getShader();
        if (h.blockTime > 0) {
            batch.setShader(Game.SHADERS.load("hit_flash"));
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
        return isUsing();
    }
}
