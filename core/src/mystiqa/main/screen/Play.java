package mystiqa.main.screen;

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

public class Play extends Screen {
    private static Play instance;

    public Array<Being> beings;
    public Array<Chunk> chunks;

    public Array<Entity> entities;

    public WorldGenerator worldGenerator;

    public Being player;

    public float screenShake;

    private Play() {

    }

    @Override
    public void create() {
        super.create();

        beings = new Array<Being>();
        chunks = new Array<Chunk>();

        entities = new Array<Entity>();

        worldGenerator = new WorldGenerator();

        Humanoid h = (Humanoid) Resources.getInstance().getBeing("Human");
        h.controlledByPlayer = true;
        h.z = 100 * 8;
        player = h;

        addBeing(h);
    }

    @Override
    public void update() {
        super.update();

        // Generate new chunks
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    int cx = player.getChunkX() + x * Chunk.WIDTH;
                    int cy = player.getChunkY() + y * Chunk.HEIGHT;
                    int cz = player.getChunkZ() + z * Chunk.DEPTH;

                    if (getChunk(cx, cy, cz) == null) {
                        Chunk c = new Chunk();

                        c.x = cx;
                        c.y = cy;
                        c.z = cz;

                        worldGenerator.generateChunk(c);
                        chunks.add(c);
                    }
                }
            }
        }

        entities.clear();

        for (Being b : beings) {
            entities.add(b);
        }

        for (int cx = -1; cx <= 1; cx++) {
            for (int cy = -1; cy <= 1; cy++) {
                for (int cz = 0; cz <= 0; cz++) {
                    Chunk c = getChunk(player.getChunkX() + cx * Chunk.WIDTH, player.getChunkY() + cy * Chunk.HEIGHT, player.getChunkZ() + cz * Chunk.DEPTH);

                    for (int x = 0; x < c.tiles.length; x++) {
                        for (int y = 0; y < c.tiles[0].length; y++) {
                            for (int z = 0; z < c.tiles[0][0].length; z++) {
                                Tile t = c.getTile(x, y, z);

                                if (t != null) {
                                    boolean surrounded = getTile(x - 1, y, z) != null && getTile(x + 1, y, z) != null && getTile(x, y - 1, z) != null && getTile(x, y + 1, z) != null && getTile(x, y, z + 1) != null;
                                    if (surrounded || Math.abs(c.x + x - player.getTileX()) > 12) {
                                        break;
                                    } else {
                                        entities.add(t);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (screenShake <= 0) {
            for (int i = 0; i < entities.size; i++) {
                entities.get(i).update();
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

            // Transform tile position to camera position
            float xx = cam.position.x + 64 - t.x;
            float yy = cam.position.y + 36 - (t.y + t.z);

            float range = 16;
            if (xx <= -range || xx > 128 + range || yy <= -range || yy >= 72 + range) {
                entities.removeValue(t, true);
            } else if (getTile(x, y, z + 1) != null && getTile(x, y - 1, z) != null) {
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

        batch.setColor(Resources.getInstance().getColor("White"));
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

    public Chunk getChunk(int x, int y, int z) {
        for (Chunk c : chunks) {
            if (x >= c.x && x < c.x + Chunk.WIDTH && y >= c.y && y < c.y + Chunk.HEIGHT && z >= c.z && z < c.z + Chunk.DEPTH) {
                return c;
            }
        }

        return null;
    }

    public Tile getTile(int x, int y, int z) {
        Chunk c = getChunk(x, y, z);

        if (c != null) {
            return c.getTile(x - c.x, y - c.y, z - c.z);
        }

        return null;
    }

    public void setTile(Tile t, int x, int y, int z) {
        Chunk c = getChunk(x, y, z);

        if (c != null) {
            c.setTile(t, x - c.x, y - c.y, c.z - z);
        }
    }

    public static Play getInstance() {
        if (instance == null) {
            instance = new Play();
        }

        return instance;
    }
}
