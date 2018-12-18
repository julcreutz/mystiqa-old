package game.main.site.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import game.loader.SheetLoader;
import game.main.Game;
import game.main.site.SiteData;

public class Humanoid extends SiteEntity {
    public TextureRegion[][] feet;
    public TextureRegion[][] body;
    public TextureRegion[][] head;

    public float animSpeed;

    public Color color;

    public int dir;
    public float time;

    public Humanoid() {
        hitbox.setSize(4, 2);
        hx = 2;
        hy = 1;
    }

    @Override
    public void update(SiteData site) {
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
            velX = MathUtils.cosDeg(angle) * 16f;
            velY = MathUtils.sinDeg(angle) * 16f;

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

            time += Game.delta();
        }

        super.update(site);
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        int step = MathUtils.floor(time * animSpeed);

        float y = this.y + (step % 2 != 0 ? -1 : 0);

        batch.setColor(color);

        switch (dir) {
            case 0:
                // Left foot
                batch.draw(feet[(step + 2) % feet.length][dir], x, y);

                // Left arm
                batch.draw(body[1 + step % (body.length - 1)][dir], x, y);

                // Torso
                batch.draw(body[0][dir], x, y);

                // Right foot
                batch.draw(feet[step % feet.length][dir], x, y);

                // Head
                batch.draw(head[step % head.length][dir], x, y);

                // Right arm
                batch.draw(body[1 + (step + 2) % (body.length - 1)][dir], x, y);

                break;
            case 2:
                // Right foot
                batch.draw(feet[step % feet.length][dir], x, y);

                // Right arm
                batch.draw(body[1 + (step + 2) % (body.length - 1)][dir], x, y);

                // Torso
                batch.draw(body[0][dir], x, y);

                // Left foot
                batch.draw(feet[(step + 2) % feet.length][dir], x, y);

                // Head
                batch.draw(head[step % head.length][dir], x, y);

                // Left arm
                batch.draw(body[1 + step % (body.length - 1)][dir], x, y);

                break;
            case 1:
            case 3:
                // Left foot
                batch.draw(feet[(step + 2) % feet.length][dir], x, y);

                // Right foot
                batch.draw(feet[step % feet.length][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                // Torso
                batch.draw(body[0][dir], x, y);

                // Left arm
                batch.draw(body[1 + step % (body.length - 1)][dir], x, y);

                // Right arm
                batch.draw(body[1 + (step + 2) % (body.length - 1)][dir], x, y, 4, 4, 8, 8, -1, 1, 0);

                // Head
                batch.draw(head[step % head.length][dir], x, y);

                break;
        }

        batch.setColor(1, 1, 1, 1);
    }
}
