package mystiqa.main.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import mystiqa.Perlin;
import mystiqa.Resources;
import mystiqa.entity.Entity;
import mystiqa.entity.being.Being;
import mystiqa.entity.tile.Chunk;
import mystiqa.entity.tile.Tile;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.item.equipable.Equipable;
import mystiqa.main.Game;

import java.util.Comparator;

public class PlayScreen extends Screen {
    public Array<Being> beings;
    public Array<Chunk> chunks;

    public Array<Entity> entities;

    public Entity player;

    public float screenShake;

    @Override
    public void create() {
        super.create();

        beings = new Array<Being>();
        chunks = new Array<Chunk>();

        entities = new Array<Entity>();

        Humanoid h = (Humanoid) Resources.getBeing("Human");

        if (h != null) {
            h.controlledByPlayer = true;

            h.z = 80 * 8;

            ((Equipable) (Resources.getItem("PlateArmor"))).equip(h);
            h.bodyArmor.material = Resources.getMaterial("Iron");

            ((Equipable) (Resources.getItem("Greaves"))).equip(h);
            h.feetArmor.material = Resources.getMaterial("Iron");

            ((Equipable) (Resources.getItem("Helmet"))).equip(h);
            h.headArmor.material = Resources.getMaterial("Iron");

            addBeing(h);

            player = h;
        }
    }

    @Override
    public void update() {
        super.update();

        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                int chunkX = MathUtils.floor((float) player.getTileX() / Chunk.WIDTH) * Chunk.WIDTH + x * Chunk.WIDTH;
                int chunkY = MathUtils.floor((float) player.getTileY() / Chunk.HEIGHT) * Chunk.HEIGHT + y * Chunk.HEIGHT;
                int chunkZ = MathUtils.floor((float) player.getTileZ() / Chunk.DEPTH) * Chunk.DEPTH;

                if (getChunk(chunkX, chunkY, chunkZ) == null) {
                    Chunk c = new Chunk(chunkX, chunkY, chunkZ);

                    for (int xx = 0; xx < c.tiles.length; xx++) {
                        for (int yy = 0; yy < c.tiles[0].length; yy++) {
                            for (int zz = 0; zz <= 10 + Perlin.noise((c.x + xx) * .05f, (c.y + yy) * .05f) * 10f; zz++) {
                                Tile t = Resources.getTile("Grass");
                                t.x = (c.x + xx) * 8;
                                t.y = (c.y + yy) * 8;
                                t.z = (c.z + zz) * 8;

                                c.tiles[xx][yy][zz] = t;
                            }
                        }
                    }
                    chunks.add(c);
                }
            }
        }

        entities.clear();

        for (Being b : beings) {
            entities.add(b);
        }

        int xView = 16;
        int yView = 9;
        int zView = 18;

        for (int x = player.getTileX() - xView; x < player.getTileX() + xView; x++) {
            for (int y = player.getTileY() - yView; y < player.getTileY() + yView; y++) {
                for (int z = player.getTileZ() + zView; z >= player.getTileZ() - zView; z--) {
                    Tile t = getTile(x, y, z);

                    if (t != null) {
                        entities.add(t);
                        break;
                    }
                }
            }
        }

        entities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                int z = Float.compare(o1.z, o2.z);
                int y = Float.compare(o2.y, o1.y);
                return z == 0 ? y : z;
            }
        });

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
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        for (Entity e : entities) {
            e.render(batch);
            //e.hitbox.render(batch);
        }

        batch.setShader(null);
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
            return c.tiles[x - c.x][y - c.y][z - c.z];
        }

        return null;
    }
}
