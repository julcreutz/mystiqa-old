package mystiqa.main.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import mystiqa.Resources;
import mystiqa.entity.Entity;
import mystiqa.entity.being.Being;
import mystiqa.entity.Tile;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.item.equipable.Equipable;
import mystiqa.main.Game;

import java.util.Comparator;

public class PlayScreen extends Screen {
    public Array<Being> beings;
    public Tile[][][] tiles;

    public Array<Entity> entities;

    public float screenShake;

    @Override
    public void create() {
        super.create();

        beings = new Array<Being>();
        tiles = new Tile[16][9][256];

        entities = new Array<Entity>();

        Humanoid h = (Humanoid) Resources.getBeing("Human");

        if (h != null) {
            h.controlledByPlayer = true;

            h.z = 16;

            ((Equipable) (Resources.getItem("BattleAxe"))).equip(h);
            ((Equipable) (Resources.getItem("MetalShield"))).equip(h);
            ((Equipable) (Resources.getItem("Greaves"))).equip(h);
            ((Equipable) (Resources.getItem("PlateArmor"))).equip(h);
            ((Equipable) (Resources.getItem("Helmet"))).equip(h);

            addBeing(h);
        }

        for (int z = 0; z < 3; z++) {
            for (int x = z; x < 16 - z; x++) {
                for (int y = z; y < 9 - z; y++) {
                    setTile(Resources.getTile("Grass"), x, y, z);
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();

        entities.clear();

        for (Being b : beings) {
            entities.add(b);
        }

        for (int x = 0; x < tiles.length; x++) {
            for (int y = 0; y < tiles[0].length; y++) {
                for (int z = 0; z < 9; z++) {
                    Tile t = tiles[x][y][z];

                    if (t != null) {
                        entities.add(tiles[x][y][z]);
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

        cam.position.x = 64 + MathUtils.random(-screenShake, screenShake);
        cam.position.y = 36 + MathUtils.random(-screenShake, screenShake);

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

    public void setTile(Tile t, int x, int y, int z) {
        t.x = x * 8;
        t.y = y * 8;
        t.z = z * 8;
        tiles[x][y][z] = t;
    }

    public Tile getTile(int x, int y, int z) {
        return x >= 0 && x < tiles.length && y >= 0 && y < tiles[0].length && z >= 0 && z < tiles[0][0].length ? tiles[x][y][z] : null;
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
}
