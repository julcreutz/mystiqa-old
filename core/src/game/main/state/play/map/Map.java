package game.main.state.play.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.Play;
import game.main.state.play.map.entity.Entity;
import game.main.state.play.map.tile.Tile;
import game.main.state.play.map.tile.TileType;

public abstract class Map implements Serializable {
    public static final float CAM_SPEED = 1.5f;

    public static final int X_VIEW = 10;
    public static final int Y_VIEW = 8;

    public TileManager tiles;

    public Array<Entity> entities;
    public Array<Entity> invisibleEntities;

    public Entity player;

    public int x0;
    public int x1;
    public int y0;
    public int y1;

    public float camX;
    public float camY;

    public float toCamX;
    public float toCamY;

    public float camPosX;
    public float camPosY;

    public float camTime;

    public Map parent;
    public Array<Map> children;

    public Map() {
        tiles = new TileManager();
        children = new Array<Map>();
    }

    public void update(Play play) {
        toCamX = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        toCamY = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / (Game.HEIGHT - 8)) * (Game.HEIGHT - 8);

        if (camX != toCamX || camY != toCamY) {
            if (camTime == 0) {
                camTime = 1;
            }

            camTime -= Game.getDelta() * CAM_SPEED;

            float p = MathUtils.clamp(1 - camTime, 0, 1);

            camPosX = MathUtils.lerp(camX, toCamX, p);
            camPosY = MathUtils.lerp(camY, toCamY, p);

            if (camTime < 0) {
                camX = toCamX;
                camY = toCamY;

                camTime = 0;
            }
        }

        x0 = MathUtils.clamp(MathUtils.floor(play.cam.position.x / 8f) - X_VIEW, 0, tiles.getWidth());
        x1 = MathUtils.clamp(x0 + X_VIEW * 2, 0, tiles.getWidth());
        y0 = MathUtils.clamp(MathUtils.floor(play.cam.position.y / 8f) - Y_VIEW, 0, tiles.getHeight());
        y1 = MathUtils.clamp(y0 + Y_VIEW * 2, 0, tiles.getHeight());

        tiles.update(this, x0, x1, y0, y1);

        for (int i = invisibleEntities.size - 1; i >= 0; i--) {
            Entity e = invisibleEntities.get(i);

            if (isVisible(e)) {
                entities.add(e);
                invisibleEntities.removeIndex(i);
            }
        }

        for (int i = entities.size - 1; i >= 0; i--) {
            Entity e = entities.get(i);

            if (!isVisible(e)) {
                invisibleEntities.add(e);
                entities.removeIndex(i);
                continue;
            }

            if (!e.updated) {
                e.update(this);
                e.updated = true;
            } else if (!isCamMoving()) {
                e.update(this);
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.setShader(null);

        tiles.render(batch, x0, x1, y0, y1, 0, 1);

        batch.setShader(null);

        for (Entity e : entities) {
            e.render(batch);
        }

        batch.setShader(null);

        tiles.render(batch, x0, x1, y0, y1, 1, tiles.getDepth());

        batch.draw(Game.SPRITE_SHEETS.load("GuiLayer").sheet[0][0], camPosX - Game.WIDTH * .5f, camPosY + Game.HEIGHT * .5f - 8, Game.WIDTH, 8);
    }

    public void positionCam() {
        camX = toCamX = camPosX = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        camY = toCamY = camPosY = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / (Game.HEIGHT - 8)) * (Game.HEIGHT - 8);
    }

    public boolean isCamMoving() {
        return camX != toCamX || camY != toCamY;
    }

    public void add(Entity e) {
        invisibleEntities.add(e);
        e.onAdded(this);
    }

    public boolean isVisible(Entity e) {
        return e.x >= x0 * 8 && e.x < x1 * 8 && e.y >= y0 * 8 && e.y < y1 * 8;
    }

    public abstract void generate();
}
