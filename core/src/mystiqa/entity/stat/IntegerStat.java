package mystiqa.entity.stat;

import com.badlogic.gdx.utils.JsonValue;

public class IntegerStat extends Stat {
    public int value;

    @Override
    public void deserialize(JsonValue json) {
        super.deserialize(json);

        value = json.asInt();
    }
}
