package game.main.state.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import game.main.Game;
import game.main.state.GameState;
import game.main.gen.WorldGenerator;
import game.main.state.play.entity.Entity;
import game.main.state.play.tile.Tile;
import game.main.state.play.tile.TileType;

public class Play extends GameState {
    public static final float CAM_SPEED = 1.5f;

    public static final int X_VIEW = 10;
    public static final int Y_VIEW = 8;

    public Tile[][][] tiles;
    public Rectangle[][] solidTiles;

    public Array<Entity> entities;
    public Array<Entity> invisibleEntities;

    public Entity player;

    public int x0;
    public int x1;
    public int y0;
    public int y1;

    public WorldGenerator worldGenerator;

    public float camX;
    public float camY;

    public float toCamX;
    public float toCamY;

    public float camTime;

    @Override
    public void create() {
        super.create();

        worldGenerator = new WorldGenerator(this);
        worldGenerator.generate();
    }

    @Override
    public void update(Game g) {
        super.update(g);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            worldGenerator.generate();
        }

        toCamX = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        toCamY = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / (Game.HEIGHT - 8)) * (Game.HEIGHT - 8);

        if (camX != toCamX || camY != toCamY) {
            if (camTime == 0) {
                camTime = 1;
            }

            camTime -= Game.delta() * CAM_SPEED;

            float p = MathUtils.clamp(1 - camTime, 0, 1);

            cam.position.x = MathUtils.lerp(camX, toCamX, p);
            cam.position.y = MathUtils.lerp(camY, toCamY, p);

            if (camTime < 0) {
                camX = toCamX;
                camY = toCamY;

                camTime = 0;
            }
        }

        cam.update();

        x0 = MathUtils.clamp(MathUtils.floor(cam.position.x / 8f) - X_VIEW, 0, tiles.length);
        x1 = MathUtils.clamp(x0 + X_VIEW * 2, 0, tiles.length);
        y0 = MathUtils.clamp(MathUtils.floor(cam.position.y / 8f) - Y_VIEW, 0, tiles[0].length);
        y1 = MathUtils.clamp(y0 + Y_VIEW * 2, 0, tiles[0].length);

        for (int x = 0; x < solidTiles.length; x++) {
            for (int y = 0; y < solidTiles[0].length; y++) {
                Tile t = tiles[x][y][0];

                if (t != null) {
                    if (t.type.solid && solidTiles[x][y] == null) {
                        solidTiles[x][y] = new Rectangle(x * 8, y * 8, 8, 8);
                    } else if (!t.type.solid && solidTiles[x][y] != null) {
                        solidTiles[x][y] = null;
                    }
                }
            }
        }

        for (int x = x0; x < x1; x++) {
            for (int y = y1 - 1; y >= y0; y--) {
                for (int z = 0; z < tiles[0][0].length; z++) {
                    Tile tile = tiles[x][y][z];

                    if (tile != null) {
                        if (!tile.updated) {
                            tile.update(this);
                            tile.updated = true;
                        } else if (!isCamMoving()) {
                            tile.update(this);
                        }
                    }
                }
            }
        }

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

    @Override
    public void renderToBuffer() {
        super.renderToBuffer();

        batch.setShader(null);

        for (int x = x0; x < x1; x++) {
            for (int y = y1 - 1; y >= y0; y--) {
                Tile tile = tiles[x][y][0];

                if (tile != null) {
                    tile.render(batch);
                }
            }
        }

        batch.setShader(null);

        for (Entity e : entities) {
            e.render(batch);
        }

        batch.setShader(null);

        for (int x = x0; x < x1; x++) {
            for (int y = y1 - 1; y >= y0; y--) {
                for (int z = 1; z < tiles[0][0].length; z++) {
                    Tile tile = tiles[x][y][z];

                    if (tile != null) {
                        tile.render(batch);
                    }
                }
            }
        }

        batch.draw(Game.SPRITE_SHEETS.load("GuiLayer").sheet[0][0], cam.position.x - Game.WIDTH * .5f, cam.position.y + Game.HEIGHT * .5f - 8, Game.WIDTH, 8);
    }

    public Tile tileAt(int x, int y, int z) {
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0][0].length ? tiles[x][y][z] : null;
    }

    public void placeTile(Tile tile, int x, int y, int z) {
        if (x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0].length) {
            tile.x = x;
            tile.y = y;
            tile.z = z;
            tiles[x][y][z] = tile;
        }
    }

    public void placeTile(TileType type, int x, int y, int z) {
        placeTile(new Tile(type), x, y, z);
    }

    public void positionCam() {
        camX = toCamX = cam.position.x = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        camY = toCamY = cam.position.y = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / (Game.HEIGHT - 8)) * (Game.HEIGHT - 8);

        cam.update();
    }

    public boolean isCamMoving() {
        return camX != toCamX || camY != toCamY;
    }

    public boolean isFree(Tile[][][] tiles, int x, int y, int z, int r) {
        for (int xx = -r; xx <= r; xx++) {
            for (int yy = -r; yy <= r; yy++) {
                Tile t = tileAt(x + xx, y + yy, z);

                if (t != null && t.type.solid) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isFree(int x, int y, int z, int r) {
        return isFree(tiles, x, y, z, r);
    }

    public int countSolid(int x, int y, int z, int r) {
        int n = 0;

        for (int xx = -r; xx <= r; xx++) {
            for (int yy = -r; yy <= r; yy++) {
                Tile t = tileAt(x + xx, y + yy, z);

                if (t != null && t.type.solid) {
                    n++;
                }
            }
        }

        return n;
    }

    public void add(Entity e) {
        invisibleEntities.add(e);
        e.onAdded(this);
    }

    public boolean isVisible(Entity e) {
        return e.x >= x0 * 8 && e.x < x1 * 8 && e.y >= y0 * 8 && e.y < y1 * 8;
    }

    public Tile[][][] copyTiles() {
        Tile[][][] tiles = new Tile[this.tiles.length][this.tiles[0].length][this.tiles[0][0].length];

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                System.arraycopy(this.tiles[x][y], 0, tiles[x][y], 0, tiles[0][0].length);
            }
        }

        return tiles;
    }
}
