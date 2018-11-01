package mystiqa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.entity.Entity;
import mystiqa.entity.humanoid.Humanoid;
import mystiqa.entity.humanoid.HumanoidRace;
import mystiqa.item.Item;
import mystiqa.item.equipable.armor.BodyArmor;
import mystiqa.item.equipable.armor.FeetArmor;
import mystiqa.item.equipable.armor.HeadArmor;
import mystiqa.item.equipable.hand.left.Shield;
import mystiqa.item.equipable.hand.right.MeleeWeapon;
import mystiqa.main.Game;

public class Resources {
    public static TextureRegion[][] getSpriteSheet(String name) {
        Array<FileHandle> files = new Array<FileHandle>();
        Game.getFiles(Gdx.files.internal("data/sprite_sheets/"), files);

        for (FileHandle file : files) {
            if (file.nameWithoutExtension().equals(name)) {
                JsonValue root = new JsonReader().parse(file);

                Texture t = new Texture(Gdx.files.internal(root.getString("path")));

                int splitX = root.getInt("splitX");
                int splitY = root.getInt("splitX");

                TextureRegion[][] spriteSheet = new TextureRegion[t.getWidth() / splitX][t.getHeight() / splitY];

                for (int x = 0; x < spriteSheet.length; x++) {
                    for (int y = 0; y < spriteSheet[0].length; y++) {
                        spriteSheet[x][y] = new TextureRegion(t, x * splitX, y * splitY, splitX, splitY);
                    }
                }

                return spriteSheet;
            }
        }

        return null;
    }

    public static Item getItem(String name) {
        Array<FileHandle> files = new Array<FileHandle>();
        Game.getFiles(Gdx.files.internal("data/items/"), files);

        for (FileHandle file : files) {
            if (file.nameWithoutExtension().equals(name)) {
                JsonValue json = new JsonReader().parse(file);

                String inherit = json.getString("inherit");
                Item item = null;

                if (inherit.equals("MeleeWeapon")) {
                    item = new MeleeWeapon();
                } else if (inherit.equals("Shield")) {
                    item = new Shield();
                } else if (inherit.equals("FeetArmor")) {
                    item = new FeetArmor();
                } else if (inherit.equals("BodyArmor")) {
                    item = new BodyArmor();
                } else if (inherit.equals("HeadArmor")) {
                    item = new HeadArmor();
                }

                if (item != null) {
                    item.deserialize(json);
                    return item;
                }
            }
        }

        return null;
    }

    public static HumanoidRace getHumanoidRace(String name) {
        Array<FileHandle> files = new Array<FileHandle>();
        Game.getFiles(Gdx.files.internal("data/humanoid_races/"), files);

        for (FileHandle file : files) {
            if (file.nameWithoutExtension().equals(name)) {
                HumanoidRace race = new HumanoidRace();
                race.deserialize(new JsonReader().parse(file));
                return race;
            }
        }

        return null;
    }

    public static Entity getEntity(String name) {
        Array<FileHandle> files = new Array<FileHandle>();
        Game.getFiles(Gdx.files.internal("data/entities/"), files);

        for (FileHandle file : files) {
            if (file.nameWithoutExtension().equals(name)) {
                JsonValue json = new JsonReader().parse(file);

                String inherit = json.getString("inherit");
                Entity e = null;

                if (inherit.equals("Humanoid")) {
                    e = new Humanoid();
                }

                if (e != null) {
                    e.deserialize(json);
                    return e;
                }
            }
        }

        return null;
    }
}
