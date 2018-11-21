package mystiqa.main.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import mystiqa.Assets;
import mystiqa.entity.Entity;
import mystiqa.entity.being.Being;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.world.Chunk;
import mystiqa.entity.tile.Tile;
import mystiqa.main.Game;
import mystiqa.world.WorldGenerator;

import java.util.Comparator;

public class Play extends Screen {
    public static final float CAM_SPEED = 10f;

    private static Play instance;

    public Array<Being> beings;
    public Chunk[][][] chunks;

    public Array<Entity> entities;

    public WorldGenerator worldGenerator;

    public Being player;

    public float screenShake;

    private int playerChunkX;
    private int playerChunkY;
    private int playerChunkZ;

    private int playerTileX;
    private int playerTileY;
    private int playerTileZ;

    private Play() {

    }

    @Override
    public void create() {
        super.create();

        beings = new Array<Being>();
        chunks = new Chunk[256][256][16];

        entities = new Array<Entity>();

        worldGenerator = Assets.getInstance().getWorldGenerator("WorldGenerator");

        Humanoid h = (Humanoid) Assets.getInstance().getBeing("Human");
        h.controlledByPlayer = true;
        h.z = 64 * Chunk.DEPTH;
        h.x = chunks.length * .5f * Chunk.WIDTH * 8;
        h.y = chunks[0].length * .5f * Chunk.HEIGHT * 8;
        player = h;

        addBeing(h);
    }

    @Override
    public void update() {
        super.update();

        System.out.println(worldGenerator.getBiome(player.getTileX(), player.getTileY()).name + ", " + worldGenerator.getTerrain(player.getTileX(), player.getTileY()).name);

        if (player.getChunkX() != playerChunkX || player.getChunkY() != playerChunkY || player.getChunkZ() != playerChunkZ) {
            // Generate new chunks
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        int cx = MathUtils.clamp(player.getChunkX() + x * Chunk.WIDTH, 0, Integer.MAX_VALUE) / Chunk.WIDTH;
                        int cy = MathUtils.clamp(player.getChunkY() + y * Chunk.HEIGHT, 0, Integer.MAX_VALUE) / Chunk.HEIGHT;
                        int cz = MathUtils.clamp(player.getChunkZ() + z * Chunk.DEPTH, 0, Integer.MAX_VALUE) / Chunk.DEPTH;

                        if (chunks[cx][cy][cz] == null) {
                            Chunk c = new Chunk();

                            c.x = cx * Chunk.WIDTH;
                            c.y = cy * Chunk.HEIGHT;
                            c.z = cz * Chunk.DEPTH;

                            worldGenerator.generateChunk(c);
                            chunks[cx][cy][cz] = c;
                        }
                    }
                }
            }
        }

        if (player.getTileX() != playerTileX || player.getTileY() != playerTileY || player.getTileZ() != playerTileZ) {
            entities.clear();

            for (Being b : beings) {
                entities.add(b);
            }

            for (int cx = -1; cx <= 1; cx++) {
                for (int cy = -1; cy <= 1; cy++) {
                    for (int cz = -1; cz <= 1; cz++) {
                        Chunk c = getChunk(player.getChunkX() + cx * Chunk.WIDTH, player.getChunkY() + cy * Chunk.HEIGHT, player.getChunkZ() + cz * Chunk.DEPTH);

                        if (c != null) {
                            for (int x = 0; x < c.tiles.length; x++) {
                                for (int y = 0; y < c.tiles[0].length; y++) {
                                    for (int z = 0; z < c.tiles[0][0].length; z++) {
                                        Tile t = c.getTile(x, y, z);

                                        if (t != null) {
                                            float xx = t.x - cam.position.x;
                                            float yy = (t.y + t.z) - cam.position.y;

                                            if (xx >= -80 && xx <= 72 && yy >= -56 && yy <= 48) {
                                                entities.add(t);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        playerChunkX = player.getChunkX();
        playerChunkY = player.getChunkY();
        playerChunkZ = player.getChunkZ();

        playerTileX = player.getTileX();
        playerTileY = player.getTileY();
        playerTileZ = player.getTileZ();

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

        // Camera logic
        cam.position.x = player.x + MathUtils.random(-screenShake, screenShake) + 4;
        cam.position.y = player.y + player.z + MathUtils.random(-screenShake, screenShake) + 4;

        cam.update();

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

        batch.setColor(Assets.getInstance().getColor("White"));
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

    public Array<Tile> getSolidTiles() {
        Array<Tile> solid = new Array<Tile>();

        for (Tile t : getTiles()) {
            if (t.type.solid) {
                solid.add(t);
            }
        }

        return solid;
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
        int xx = x / Chunk.WIDTH;
        int yy = y / Chunk.HEIGHT;
        int zz = z / Chunk.DEPTH;

        return xx >= 0 && xx < chunks.length && yy >= 0 && yy < chunks[0].length && zz >= 0 && zz < chunks[0][0].length ? chunks[xx][yy][zz] : null;
    }

    public Tile getTile(int x, int y, int z) {
        Chunk c = getChunk(x, y, z);

        if (c != null) {
            return c.getTile(x - c.x, y - c.y, z - c.z);
        }

        return null;
    }

    public static Play getInstance() {
        if (instance == null) {
            instance = new Play();
        }

        return instance;
    }
}
