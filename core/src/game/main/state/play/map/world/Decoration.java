package game.main.state.play.map.world;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.map.structure.Structure;

public class Decoration implements Serializable {
    public float chance;
    public int freeRadius;

    public Structure structure;

    public Decoration(JsonValue json) {
        deserialize(json);
    }

    @Override
    public void deserialize(JsonValue json) {
        if (json.has("chance")) {
            chance = json.getFloat("chance");
        }

        if (json.has("freeRadius")) {
            freeRadius = json.getInt("freeRadius");
        }

        if (json.has("structure")) {
            structure = Game.STRUCTURES.load(json.getString("structure"));
        }
    }
}
