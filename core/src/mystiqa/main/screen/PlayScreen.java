package mystiqa.main.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import mystiqa.Resources;
import mystiqa.entity.Entity;
import mystiqa.entity.humanoid.Humanoid;
import mystiqa.item.equipable.armor.BodyArmor;
import mystiqa.item.equipable.armor.FeetArmor;
import mystiqa.item.equipable.armor.HeadArmor;
import mystiqa.item.equipable.hand.left.LeftHand;
import mystiqa.item.equipable.hand.right.RightHand;

import java.util.Comparator;

public class PlayScreen extends Screen {
    public Array<Entity> entities;

    @Override
    public void create() {
        super.create();

        entities = new Array<Entity>();

        Humanoid h = (Humanoid) Resources.getEntity("Human");

        if (h != null) {
            h.controlledByPlayer = true;

            h.rightHand = (RightHand) Resources.getItem("Longsword");
            h.leftHand = (LeftHand) Resources.getItem("MetalShield");
            h.feetArmor = (FeetArmor) Resources.getItem("Greaves");
            h.bodyArmor = (BodyArmor) Resources.getItem("PlateArmor");
            h.headArmor = (HeadArmor) Resources.getItem("Helmet");

            addEntity(h);
        }
    }

    @Override
    public void update() {
        super.update();

        for (int i = 0; i < entities.size; i++) {
            Entity e = entities.get(i);
            e.update(this);
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        super.render(batch);

        entities.sort(new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return Float.compare(o2.y, o1.y);
            }
        });

        for (Entity e : entities) {
            e.render(batch);
        }
    }

    public void addEntity(Entity e) {
        entities.add(e);
        e.onAdded();
    }
}
