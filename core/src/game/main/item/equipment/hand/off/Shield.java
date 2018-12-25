package game.main.item.equipment.hand.off;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import game.main.play.entity.humanoid.Humanoid;
import game.main.stat.RelativeStat;
import game.main.stat.StatType;

public class Shield extends OffHand {
    public TextureRegion[] images;
    public int imageIndex;

    public float x;
    public float y;

    public RelativeStat slowdown;

    public Shield() {
        slowdown = new RelativeStat(StatType.SPEED, .5f);
        armIndex = 3;
    }

    @Override
    public void update(Humanoid h) {
        super.update(h);

        if (using()) {
            imageIndex = h.dir;

            switch (imageIndex) {
                case 0:
                    x = h.x + 7;
                    y = h.y + 0;
                    renderBehind = true;
                    break;
                case 2:
                    x = h.x - 7;
                    y = h.y + 0;
                    renderBehind = false;
                    break;
                case 1:
                    x = h.x - 2;
                    y = h.y + 1;
                    renderBehind = true;
                    break;
                case 3:
                    x = h.x + 2;
                    y = h.y - 2;
                    renderBehind = false;
                    break;
            }
        } else {
            imageIndex = (h.dir + 1) % 4;

            switch (imageIndex) {
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

        if (h.step % 2 != 0) {
            y--;
        }
    }

    @Override
    public void onStartUse(Humanoid h) {
        super.onStartUse(h);

        if (!h.stats.containsRelative(slowdown)) {
            h.stats.addRelative(slowdown);
        }
    }

    @Override
    public void onFinishUse(Humanoid h) {
        super.onFinishUse(h);

        if (h.stats.containsRelative(slowdown)) {
            h.stats.removeRelative(slowdown);
        }
    }

    @Override
    public void render(SpriteBatch batch, Humanoid h) {
        super.render(batch, h);

        batch.draw(images[imageIndex], x, y);
    }
}
