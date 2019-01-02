package game.main.state.play.map.world;

import com.badlogic.gdx.utils.JsonValue;
import game.loader.Serializable;
import game.main.Game;
import game.main.state.play.map.entity.Entity;

import java.util.Random;

public class MonsterComponent implements Serializable {
    public String[] monsters;
    public int[] count;

    public MonsterComponent(JsonValue json) {
        deserialize(json);
    }

    public Entity getRandomMonster(Random rand) {
        return Game.ENTITIES.load(monsters[rand.nextInt(monsters.length)]);
    }

    public int getRandomCount(Random rand) {
        return count[rand.nextInt(count.length)];
    }

    @Override
    public void deserialize(JsonValue json) {
        JsonValue monsters = json.get("monsters");
        if (monsters != null) {
            this.monsters = monsters.asStringArray();
        }

        JsonValue count = json.get("count");
        if (monsters != null) {
            this.count = count.asIntArray();
        }
    }
}
