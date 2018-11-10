package mystiqa;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import mystiqa.entity.being.Being;
import mystiqa.entity.being.humanoid.Humanoid;
import mystiqa.entity.being.humanoid.HumanoidRace;
import mystiqa.entity.being.slime.Slime;
import mystiqa.item.Item;
import mystiqa.item.equipable.armor.BodyArmor;
import mystiqa.item.equipable.armor.FeetArmor;
import mystiqa.item.equipable.armor.HeadArmor;
import mystiqa.item.equipable.hand.left.Shield;
import mystiqa.item.equipable.hand.right.MeleeWeapon;
import mystiqa.item.equipable.material.Material;
import mystiqa.main.Game;

public class Resources {
    public static TextureRegion[][] getSpriteSheet(String name) {
        for (FileHandle file : Game.getFiles(Gdx.files.internal("data/sprite_sheets/"))) {
            if (file.nameWithoutExtension().equals(name)) {
                JsonValue root = new JsonReader().parse(file);

                Texture t = new Texture(Gdx.files.internal(root.getString("path")));

                int splitX = root.getInt("splitX");
                int splitY = root.getInt("splitY");

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
        for (FileHandle file : Game.getFiles(Gdx.files.internal("data/items/"))) {
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
        for (FileHandle file : Game.getFiles(Gdx.files.internal("data/humanoid_races/"))) {
            if (file.nameWithoutExtension().equals(name)) {
                HumanoidRace race = new HumanoidRace();
                race.deserialize(new JsonReader().parse(file));
                return race;
            }
        }

        return null;
    }

    public static Being getBeing(String name) {
        for (FileHandle file : Game.getFiles(Gdx.files.internal("data/beings/"))) {
            if (file.nameWithoutExtension().equals(name)) {
                JsonValue json = new JsonReader().parse(file);

                String inherit = json.getString("inherit");
                Being e = null;

                if (inherit.equals("Humanoid")) {
                    e = new Humanoid();
                } else if (inherit.equals("Slime")) {
                    e = new Slime();
                }

                if (e != null) {
                    e.deserialize(json);
                    return e;
                }
            }
        }

        return null;
    }

    public static Material getMaterial(String name) {
        for (FileHandle file : Game.getFiles(Gdx.files.internal("data/materials/"))) {
            if (file.nameWithoutExtension().equals(name)) {
                Material m = new Material();
                m.deserialize(new JsonReader().parse(file));

                return m;
            }
        }

        return null;
    }

    public static Color getColor(String name) {
        for (FileHandle file : Game.getFiles(Gdx.files.internal("data/colors/"))) {
            if (file.nameWithoutExtension().equals(name)) {
                JsonValue json = new JsonReader().parse(file);

                Color c = new Color();

                if (json.has("r")) {
                    c.r = json.getInt("r");
                }

                if (json.has("g")) {
                    c.g = json.getInt("g");
                }

                if (json.has("b")) {
                    c.b = json.getInt("b");
                }

                return c;
            }
        }

        return null;
    }
}
