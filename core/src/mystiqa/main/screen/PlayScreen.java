package mystiqa.main.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import mystiqa.Resources;
import mystiqa.entity.Entity;
import mystiqa.entity.being.Being;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.entity.tile.Chunk;
import mystiqa.entity.tile.Tile;
import mystiqa.main.Game;
import mystiqa.world_generation.WorldGenerator;

import java.util.Comparator;

public class PlayScreen extends Screen {
    public Array<Being> beings;
    public Array<Chunk> chunks;

    public Array<Entity> entities;

    public WorldGenerator worldGenerator;

    public Being player;

    public float screenShake;

    @Override
    public void create() {
        super.create();

        beings = new Array<Being>();
        chunks = new Array<Chunk>();

        entities = new Array<Entity>();

        worldGenerator = new WorldGenerator();

        Humanoid h = (Humanoid) Resources.getBeing("Human");
        h.controlledByPlayer = true;
        h.z = 100 * 8;
        player = h;

        addBeing(h);
    }

    @Override
    public void update() {
        super.update();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int chunkX = MathUtils.floor((player.getTileX() + x * Chunk.WIDTH) / (float) Chunk.WIDTH) * Chunk.HEIGHT;
                int chunkY = MathUtils.floor((player.getTileY() + y * Chunk.HEIGHT) / (float) Chunk.HEIGHT) * Chunk.HEIGHT;

                if (getChunk(chunkX, chunkY) == null) {
                    Chunk c = new Chunk();

                    c.x = chunkX;
                    c.y = chunkY;

                    worldGenerator.generateChunk(c);
                    chunks.add(c);
                }
            }
        }

        entities.clear();

        for (Being b : beings) {
            entities.add(b);
        }

        for (int x = player.getTileX() - 10; x < player.getTileX() + 10; x++) {
            for (int y = player.getTileY() - 16; y < player.getTileY() + 8; y++) {
                for (int z = player.getTileZ() - 4; z < player.getTileZ() + 16; z++) {
                    Tile t = getTile(x, y, z);

                    if (t != null) {
                        entities.add(t);
                    }
                }
            }
        }

        if (screenShake <= 0) {
            for (int i = 0; i < entities.size; i++) {
                entities.get(i).update(this);
            }
        } else {
            screenShake -= Game.getDelta() * 10f;
            if (screenShake < 0) {
                screenShake = 0;
            }
        }

        cam.position.x = player.x + MathUtils.random(-screenShake, screenShake) + 4;
        cam.position.y = player.y + player.z + MathUtils.random(-screenShake, screenShake) + 4;

        cam.update();

        // Sort out invisible tiles
        for (Tile t : getTiles()) {
            int x = t.getTileX();
            int y = t.getTileY();
            int z = t.getTileZ();

            if (getTile(x, y, z + 1) != null && getTile(x, y - 1, z) != null) {
                entities.removeValue(t, true);
            }
        }

        // Sort first by y, then by z
        entities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                int z = Float.compare(o1.getTileZ(), o2.getTileZ());
                int y = Float.compare(o2.getTileY(), o1.getTileY());

                return z == 0 ? y : z;
            }
        });
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        for (Entity e : entities) {
            e.render(batch);
            //e.hitbox.render(batch);
        }

        batch.setColor(Resources.getColor("White"));
    }

    public void addBeing(Being e) {
        beings.add(e);
        e.onAdded();
    }

    public Array<Tile> getTiles() {
        Array<Tile> tiles = new Array<Tile>();

        for (Entity e : entities) {
            if (e instanceof Tile) {
                tiles.add((Tile) e);
            }
        }

        return tiles;
    }

    public Array<Being> getBeings() {
        Array<Being> beings = new Array<Being>();

        for (Entity e : entities) {
            if (e instanceof Being) {
                beings.add((Being) e);
            }
        }

        return beings;
    }

    public Chunk getChunk(int x, int y) {
        for (Chunk c : chunks) {
            if (x >= c.x && x < c.x + Chunk.WIDTH && y >= c.y && y < c.y + Chunk.HEIGHT) {
                return c;
            }
        }

        return null;
    }

    public Tile getTile(int x, int y, int z) {
        Chunk c = getChunk(x, y);

        if (c != null) {
            return c.getTile(x - c.x, y - c.y, z);
        }

        return null;
    }

    public void setTile(Tile t, int x, int y, int z) {
        Chunk c = getChunk(x, y);

        if (c != null) {
            c.setTile(t, x - c.x, y - c.y, z);
        }
    }
}
