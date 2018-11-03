package mystiqa.item.equipable.hand.left;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.Resources;
import mystiqa.entity.humanoid.Humanoid;

public class Shield extends LeftHand {
    public int dir;

    public float x;
    public float y;

    public TextureRegion[][] graphics;

    @Override
    public void update(Humanoid h) {
        if (using) {
            dir = h.dir;

            float[][] x = new float[][] {
                    {12, 12, 12, 12},
                    {-4, -4, -4, -4},
                    {-12, -12, -12, -12},
                    {4, 4, 4, 4}
            };

            float[][] y = new float[][] {
                    {-1, -2, -1, -2},
                    {2, 1, 2, 1},
                    {-1, -2, -1, -2},
                    {-4, -5, -4, -5}
            };

            this.x = h.x + x[dir][h.step];
            this.y = h.y + y[dir][h.step];

            step = 1;
        } else {
            dir = (h.dir + 1) % 4;

            float[][] x = new float[][] {
                    {13, 12, 13, 12},
                    {0, 1, 0, -1},
                    {-13, -12, -13, -12},
                    {0, -1, 0, 1}
            };

            float[][] y = new float[][] {
                    {-1, -2, -1, 0},
                    {1, 0, 1, 0},
                    {-1, 0, -1, -2},
                    {-4, -5, -4, -5}
            };

            this.x = h.x + x[dir][h.step];
            this.y = h.y + y[dir][h.step];

            step = h.step;
        }

        switch (dir) {
            case 0:
                behind = false;
                break;
            case 1:
                behind = true;
                break;
            case 2:
                behind = false;
                break;
            case 3:
                behind = false;
                break;
        }

        super.update(h);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        batch.draw(graphics[0][dir], x, y);
    }

    @Override
    public void use(Humanoid h) {
        super.use(h);

        h.velX *= .75f;
        h.velY *= .75f;

        h.blockDirectionChange = true;
    }

    @Override
    public void onBeginUse(Humanoid h) {
        super.onBeginUse(h);
    }

    @Override
    public void onEndUse(Humanoid h) {
        super.onEndUse(h);

        h.blockDirectionChange = false;
    }

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        if (json.has("graphics")) {
            graphics = Resources.getSpriteSheet(json.getString("graphics"));
        }
    }
}
