package game.main.item.equipment.hand.off;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import game.loader.resource.sprite_sheet.SpriteSheet;
import game.main.Game;
import game.main.state.play.map.entity.Humanoid;
import game.main.stat.Stat;

public class Shield extends OffHand {
    public SpriteSheet spriteSheet;
    public TextureRegion image;

    public float x;
    public float y;

    public Stat slowdown;

    public Shield() {
        slowdown = new Stat(Stat.Type.SPEED, 0, .5f);
        armIndex = 3;
    }

    @Override
    public void update(Humanoid h) {
        super.update(h);

        int dir;

        if (isUsing()) {
            dir = h.dir;

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
                    h.blockHitbox.set(8, 1, 0, 7);
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

        image = spriteSheet.sheet[0][dir];

        if (h.step % 2 != 0) {
            y--;
        }
    }

    @Override
    public void onStartUse(Humanoid h) {
        super.onStartUse(h);

        if (!h.statManager.stats.contains(slowdown, true)) {
            h.statManager.stats.add(slowdown);
        }
    }

    @Override
    public void onFinishUse(Humanoid h) {
        super.onFinishUse(h);

        if (h.statManager.stats.contains(slowdown, true)) {
            h.statManager.stats.removeValue(slowdown, true);
        }
    }

    @Override
    public void render(SpriteBatch batch, Humanoid h) {
        super.render(batch, h);

        batch.draw(image, x, y);
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("spriteSheet")) {
            spriteSheet = Game.SPRITE_SHEETS.load(json.getString("spriteSheet"));
        }
    }
}
