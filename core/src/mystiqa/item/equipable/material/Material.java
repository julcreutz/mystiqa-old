package mystiqa.item.equipable.material;

import com.badlogic.gdx.graphics.Color;
import mystiqa.stat.StatManager;

public class Material {
    public String name;
    public MaterialType type;

    public StatManager stats;

    public Color color;

    public Material() {
        stats = new StatManager();
    }
}
