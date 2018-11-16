package mystiqa.stat;

import com.badlogic.gdx.utils.JsonValue;

public class IntegerStat extends Stat {
    public int value;

    @Override
    public void deserialize(JsonValue json) {
        value = json.asInt();
    }
}
