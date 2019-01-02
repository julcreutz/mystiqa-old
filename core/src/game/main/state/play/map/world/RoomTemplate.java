package game.main.state.play.map.world;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;

public class RoomTemplate implements Serializable {
    public float chance;

    public float horizontalFlipChance;
    public float verticalFlipChance;

    public int width;
    public int height;

    public char[][] layout;

    public MonsterComponent monsters;

    public char[][] copyLayout() {
        char[][] layout = new char[this.layout.length][this.layout[0].length];

        for (int x = 0; x < layout.length; x++) {
            System.arraycopy(this.layout[x], 0, layout[x], 0, layout[x].length);
        }

        return layout;
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("chance")) {
            chance = json.getFloat("chance");
        }

        if (json.has("horizontalFlipChance")) {
            horizontalFlipChance = json.getFloat("horizontalFlipChance");
        }

        if (json.has("verticalFlipChance")) {
            verticalFlipChance = json.getFloat("verticalFlipChance");
        }

        if (json.has("width")) {
            width = json.getInt("width");
        }

        if (json.has("height")) {
            height = json.getInt("height");
        }

        if (json.has("layout")) {
            layout = new char[height * 4][width * 8];

            int i = 0;
            for (JsonValue row : json.get("layout")) {
                layout[i] = row.asCharArray();
                i++;
            }
        }

        JsonValue monsters = json.get("monsters");
        if (monsters != null) {
            this.monsters = new MonsterComponent(monsters);
        }
    }
}
