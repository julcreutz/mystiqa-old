package game.main.play.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import game.main.Game;
import game.main.play.Play;
import game.main.stat.StatType;
import game.main.stat.Stats;

public class Entity {
    public float x;
    public float y;

    public float velX;
    public float velY;

    public Hitbox hitbox;

    public boolean updated;

    public Stats stats;

    public Entity() {
        hitbox = new Hitbox();
        stats = new Stats();
    }

    public void update(Play site) {
        hitbox.position(this, velX * Game.delta(), 0);

        for (int x = 0; x < site.solidTiles.length; x++) {
            for (int y = 0; y < site.solidTiles[0].length; y++) {
                Rectangle solidTile = site.solidTiles[x][y];

                if (solidTile != null && hitbox.overlaps(solidTile)) {
                    if (velX > 0) {
                        this.x = solidTile.x - hitbox.width() - hitbox.offsetX;
                    } else if (velX < 0) {
                        this.x = solidTile.x + solidTile.width - hitbox.offsetX;
                    }

                    velX = 0;
                }
            }
        }

        x += velX * Game.delta();

        hitbox.position(this, 0, velY * Game.delta());

        for (int x = 0; x < site.solidTiles.length; x++) {
            for (int y = 0; y < site.solidTiles[0].length; y++) {
                Rectangle solidTile = site.solidTiles[x][y];

                if (solidTile != null && hitbox.overlaps(solidTile)) {
                    if (velY > 0) {
                        this.y = solidTile.y - hitbox.height() - hitbox.offsetY;
                    } else if (velY < 0) {
                        this.y = solidTile.y + solidTile.height - hitbox.offsetY;
                    }

                    velY = 0;
                }
            }
        }

        y += velY * Game.delta();

        if (velX != 0 || velY != 0) {
            onMove();
        }

        velX = 0;
        velY = 0;
    }

    public void render(SpriteBatch batch) {

    }

    public void onMove() {

    }
}
