package game.main.site.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import game.main.Game;
import game.main.site.SiteData;

public class SiteEntity {
    public float x;
    public float y;

    public float velX;
    public float velY;

    public Rectangle hitbox;
    public float hx;
    public float hy;

    public SiteEntity() {
        hitbox = new Rectangle();
    }

    public void update(SiteData site) {
        positionHitbox(velX * Game.delta(), 0);

        for (int x = 0; x < site.solidTiles.length; x++) {
            for (int y = 0; y < site.solidTiles[0].length; y++) {
                Rectangle solidTile = site.solidTiles[x][y];

                if (solidTile != null && hitbox.overlaps(solidTile)) {
                    if (velX > 0) {
                        this.x = solidTile.x - hitbox.width - hx;
                    } else if (velX < 0) {
                        this.x = solidTile.x + solidTile.width - hx;
                    }

                    velX = 0;
                }
            }
        }

        x += velX * Game.delta();

        positionHitbox(0, velY * Game.delta());

        for (int x = 0; x < site.solidTiles.length; x++) {
            for (int y = 0; y < site.solidTiles[0].length; y++) {
                Rectangle solidTile = site.solidTiles[x][y];

                if (solidTile != null && hitbox.overlaps(solidTile)) {
                    if (velY > 0) {
                        this.y = solidTile.y - hitbox.height - hy;
                    } else if (velY < 0) {
                        this.y = solidTile.y + solidTile.height - hy;
                    }

                    velY = 0;
                }
            }
        }

        y += velY * Game.delta();

        velX = 0;
        velY = 0;
    }

    public void render(SpriteBatch batch) {

    }

    public void positionHitbox(float x, float y) {
        hitbox.setPosition(this.x + hx + x, this.y + hy + y);
    }
}
