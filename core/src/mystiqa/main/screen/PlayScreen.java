package mystiqa.main.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import mystiqa.Resources;
import mystiqa.entity.Entity;
import mystiqa.entity.Tile;
import mystiqa.entity.humanoid.Humanoid;
import mystiqa.item.equipable.Equipable;
import mystiqa.item.equipable.armor.BodyArmor;
import mystiqa.item.equipable.armor.FeetArmor;
import mystiqa.item.equipable.armor.HeadArmor;
import mystiqa.item.equipable.hand.left.LeftHand;
import mystiqa.item.equipable.hand.right.RightHand;
import mystiqa.main.Game;

import java.util.Comparator;

public class PlayScreen extends Screen {
    public Array<Entity> entities;
    public float screenShake;

    @Override
    public void create() {
        super.create();

        entities = new Array<Entity>();

        for (int x = 1; x < 14; x++) {
            for (int y = 1; y < 5; y++) {
                Tile t = new Tile();
                t.x = x * 16;
                t.y = y * 16;
                t.z = -16;
                addEntity(t);
            }
        }

        Humanoid h = (Humanoid) Resources.getEntity("Human");

        if (h != null) {
            h.controlledByPlayer = true;

            h.x = 50;
            h.y = 50;
            h.z = 16;

            ((Equipable) (Resources.getItem("BattleAxe"))).equip(h);
            ((Equipable) (Resources.getItem("MetalShield"))).equip(h);
            ((Equipable) (Resources.getItem("Greaves"))).equip(h);
            ((Equipable) (Resources.getItem("PlateArmor"))).equip(h);
            ((Equipable) (Resources.getItem("Helmet"))).equip(h);

            addEntity(h);
        }

        addEntity(Resources.getEntity("GreenSlime"));
    }

    @Override
    public void update() {
        super.update();

        if (screenShake <= 0) {
            for (int i = 0; i < entities.size; i++) {
                Entity e = entities.get(i);
                e.update(this);
            }
        } else {
            screenShake -= Game.getDelta() * 10f;
            if (screenShake < 0) {
                screenShake = 0;
            }
        }

        cam.position.x = 128 + MathUtils.random(-screenShake, screenShake);
        cam.position.y = 72 + MathUtils.random(-screenShake, screenShake);

        cam.update();
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        entities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return Float.compare(o1.z, o2.z) + Float.compare(o2.y, o1.y);
            }
        });

        for (Entity e : entities) {
            e.render(batch);
            e.hitbox.render(batch);
        }

        batch.setShader(null);
    }

    public void addEntity(Entity e) {
        entities.add(e);
        e.onAdded();
    }
}
