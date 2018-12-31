package game.main.state.play.map;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.Play;
import game.main.state.play.map.entity.Entity;
import game.main.state.play.map.entity.EntityManager;
import game.main.state.play.map.tile.TileManager;

public abstract class Map implements Serializable {
    public class Teleport {
        public Rectangle rect;

        public Map destination;

        public float destinationX;
        public float destinationY;

        public Teleport(Map destination, float destinationX, float destinationY, float x, float y, float width, float height) {
            this.destination = destination;

            this.destinationX = destinationX;
            this.destinationY = destinationY;

            rect = new Rectangle(x, y, width, height);
        }
    }

    public static final float CAM_SPEED = 1.5f;

    public static final int X_VIEW = 10;
    public static final int Y_VIEW = 8;

    public boolean generated;

    public TileManager tiles;

    public EntityManager entities;
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

    public Array<Teleport> teleports;

    public Map() {
        tiles = new TileManager(this);
        entities = new EntityManager(this);

        teleports = new Array<Teleport>();
    }

    public void update(Play play) {
        toCamX = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        toCamY = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / (Game.HEIGHT - 8)) * (Game.HEIGHT - 8);

        if (camX != toCamX || camY != toCamY) {
            if (camTime == 0) {
                camTime = 1;
            }

            camTime -= Game.delta() * CAM_SPEED;

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

        tiles.update(x0, x1, y0, y1);
        entities.update();

        if (player.onTeleport) {
            boolean onTeleport = false;

            for (Teleport t : teleports) {
                if (t.rect.overlaps(player.hitbox.rect)) {
                    onTeleport = true;
                    break;
                }
            }

            if (!onTeleport) {
                player.onTeleport = false;
            }
        } else {
            for (Teleport t : teleports) {
                if (t.rect.overlaps(player.hitbox.rect)) {
                    player.onTeleport = true;

                    if (!t.destination.generated) {
                        t.destination.generate();
                    }

                    play.nextMap = t.destination;

                    player.x = t.destinationX;
                    player.y = t.destinationY;

                    play.nextMap.entities.addEntity(player);
                    play.nextMap.player = player;

                    entities.entities.removeValue(player, true);
                    player = null;

                    break;
                }
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.setShader(null);

        tiles.render(batch, x0, x1, y0, y1, 0, 1);

        batch.setShader(null);

        entities.render(batch);

        batch.setShader(null);

        tiles.render(batch, x0, x1, y0, y1, 1, tiles.getDepth());

        batch.draw(Game.SPRITE_SHEETS.load("GuiLayer").sheet[0][0], camPosX - Game.WIDTH * .5f, camPosY + Game.HEIGHT * .5f - 8, Game.WIDTH, 8);
    }

    public void positionCamera() {
        camX = toCamX = camPosX = Game.WIDTH * .5f + MathUtils.floor((player.x + 4) / Game.WIDTH) * Game.WIDTH;
        camY = toCamY = camPosY = Game.HEIGHT * .5f + MathUtils.floor((player.y + 4) / (Game.HEIGHT - 8)) * (Game.HEIGHT - 8);
    }

    public boolean isCamMoving() {
        return camX != toCamX || camY != toCamY;
    }

    public boolean isVisible(float x, float y) {
        return x >= x0 * 8 && x < x1 * 8 && y >= y0 * 8 && y < y1 * 8;
    }

    public boolean isVisible(Entity e) {
        return isVisible(e.x, e.y);
    }

    public void generate() {
        generated = true;
    }

    public void connect(Map destination, float fromX, float fromY, float toX, float toY) {
        teleports.add(new Teleport(destination, toX, toY, fromX, fromY, 8, 8));
        destination.teleports.add(new Teleport(this, fromX, fromY, toX, toY, 8, 8));
    }

    public abstract void placePlayer();
}
