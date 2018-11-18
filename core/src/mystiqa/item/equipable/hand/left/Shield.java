package mystiqa.item.equipable.hand.left;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import mystiqa.entity.being.humanoid.Humanoid;

public class Shield extends LeftHand {
    public int dir;

    public float x;
    public float y;
    public float z;

    public TextureRegion[][] graphics;

    @Override
    public void update(Humanoid h) {
        z = h.z;

        if (using) {
            dir = h.dir;

            float[][] x = new float[][] {
                    {7, 7, 7, 7},
                    {-2, -2, -2, -2},
                    {-7, -7, -7, -7},
                    {2, 2, 2, 2}
            };

            float[][] y = new float[][] {
                    {0, -1, 0, 1},
                    {2, 1, 2, 1},
                    {0, -1, -0, -1},
                    {-2, -3, -2, -3}
            };

            this.x = h.x + x[dir][h.step];
            this.y = h.y + y[dir][h.step];

            step = 1;

            switch (dir) {
                case 0:
                    h.defendHitbox.set(7, 0, 0, 1, 8, 8);
                    break;
                case 2:
                    h.defendHitbox.set(0, 0, 0, 1, 8, 8);
                    break;
                case 1:
                    h.defendHitbox.set(0, 7, 0, 8, 1, 8);
                    break;
                case 3:
                    h.defendHitbox.set(0, 0, 0, 8, 1, 8);
                    break;
            }
        } else {
            dir = (h.dir + 1) % 4;

            float[][] x = new float[][] {
                    {8, 7, 8, 7},
                    {0, 1, 0, -1},
                    {-8, -7, -8, -7},
                    {0, -1, 0, 1}
            };

            float[][] y = new float[][] {
                    {-1, -2, -1, 0},
                    {1, 0, 1, 0},
                    {-1, 0, -1, -2},
                    {-2, -3, -2, -3}
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

        batch.draw(graphics[0][dir], x, y + z);
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

        h.defending = true;
    }

    @Override
    public void onEndUse(Humanoid h) {
        super.onEndUse(h);

        h.blockDirectionChange = false;

        h.defending = false;
    }
}
