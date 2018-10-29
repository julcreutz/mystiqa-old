package mystiqa.main.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import mystiqa.Resources;
import mystiqa.entity.Entity;
import mystiqa.entity.Humanoid;
import mystiqa.entity.Player;
import mystiqa.item.equipable.armor.BodyArmor;
import mystiqa.item.equipable.armor.FeetArmor;
import mystiqa.item.equipable.hand.left.Shield;
import mystiqa.item.equipable.hand.right.MeleeWeapon;

public class PlayScreen extends Screen {
    public Array<Entity> entities;

    @Override
    public void create() {
        super.create();

        entities = new Array<Entity>();

        Player p = new Player();

        Shield s = new Shield();
        s.graphics = Resources.getSpriteSheet("MetalShield");
        s.equip(p);

        MeleeWeapon mw = new MeleeWeapon();
        mw.graphics = Resources.getSpriteSheet("BattleAxe");
        mw.equip(p);

        FeetArmor fa = new FeetArmor();
        fa.graphics = Resources.getSpriteSheet("Greaves");
        fa.equip(p);

        BodyArmor ba = new BodyArmor();
        ba.graphics = Resources.getSpriteSheet("PlateArmor");
        ba.equip(p);

        addEntity(p);

        addEntity(new Humanoid());
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

        for (Entity e : entities) {
            e.render(batch);
        }
    }

    public void addEntity(Entity e) {
        entities.add(e);
        e.onAdded();
    }
}
