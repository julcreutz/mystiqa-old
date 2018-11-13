package mystiqa.main.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import mystiqa.Perlin;
import mystiqa.Resources;
import mystiqa.entity.Entity;
import mystiqa.entity.being.Being;
import mystiqa.entity.being.humanoid.Human;
import mystiqa.entity.tile.Grass;
import mystiqa.entity.tile.Tile;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.item.equipable.Equipable;
import mystiqa.item.equipable.armor.body.PlateArmor;
import mystiqa.item.equipable.armor.feet.Greaves;
import mystiqa.item.equipable.armor.head.Helmet;
import mystiqa.item.equipable.material.Iron;
import mystiqa.main.Game;

import java.util.Comparator;

public class PlayScreen extends Screen {
    public Array<Being> beings;
    public Tile[][][] tiles;

    public Array<Entity> entities;

    public Being player;

    public float screenShake;

    @Override
    public void create() {
        super.create();

        beings = new Array<Being>();
        tiles = new Tile[256][256][256];

        entities = new Array<Entity>();

        Human h = new Human();

        h.controlledByPlayer = true;

        h.x = 256 * 4;
        h.y = 256 * 4;
        h.z = 64 * 8;

        new PlateArmor().equip(h);
        h.bodyArmor.material = new Iron();

        new Greaves().equip(h);
        h.feetArmor.material = new Iron();

        new Helmet().equip(h);
        h.headArmor.material = new Iron();

        addBeing(h);

        player = h;

        generate();
    }

    @Override
    public void update() {
        super.update();

        entities.clear();

        for (Being b : beings) {
            entities.add(b);
        }

        int xView = 16;
        int yView = 9;
        int zView = 9;

        for (int x = player.getTileX() - xView; x < player.getTileX() + xView; x++) {
            for (int y = player.getTileY() - yView; y < player.getTileY() + yView; y++) {
                for (int z = player.getTileZ() - zView; z < player.getTileZ() + zView; z++) {
                    Tile t = getTile(x, y, z);

                    if (t != null) {
                        entities.add(t);
                    }
                }
            }
        }

        entities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                int z = Float.compare(o1.getTileZ(), o2.getTileZ());
                int y = Float.compare(o2.getTileY(), o1.getTileY());
                return y == 0 ? z : y;
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

        // Sort out invisible tiles
        for (Tile t : getTiles()) {
            int x = t.getTileX();
            int y = t.getTileY();
            int z = t.getTileZ();

            if (getTile(x, y, z + 1) != null && getTile(x, y - 1, z) != null) {
                entities.removeValue(t, true);
            }
        }
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

    public Tile getTile(int x, int y, int z) {
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0][0].length ? tiles[x][y][z] : null;
    }

    public void setTile(Tile t, int x, int y, int z) {
        t.x = x * 8;
        t.y = y * 8;
        t.z = z * 8;

        tiles[x][y][z] = t;
    }

    public void generate() {
        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                int z = 64 + (int) (Perlin.layeredNoise(x, y, 1, .01f, 2, 1, .5f) * 20f);

                for (int zz = 0; zz <= z; zz++) {
                    setTile(new Grass(), x, y, zz);
                }
            }
        }
    }
}
