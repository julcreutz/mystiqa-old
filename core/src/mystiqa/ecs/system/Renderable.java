package mystiqa.ecs.system;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import mystiqa.ecs.EntityManager;

public interface Renderable {
    void render(EntityManager em, SpriteBatch batch);
}
