package game.main.state.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import game.main.Game;
import game.main.state.GameState;
import game.main.state.play.map.Map;

public class Play extends GameState {
    public Map map;
    public Map nextMap;

    @Override
    public void create() {
        super.create();

        nextMap = Game.MAPS.load("Overworld");
        nextMap.generate();
        nextMap.placePlayer();
    }

    @Override
    public void update(Game g) {
        super.update(g);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            nextMap = Game.MAPS.load("Overworld");
            nextMap.generate();
            nextMap.placePlayer();
        }

        if (nextMap != null) {
            map = nextMap;
            map.positionCamera();
            nextMap = null;
        }

        if (map.screenShake <= 0) {
            map.update(this);
        } else {
            map.screenShake -= Game.getDelta() * 10f;

            if (map.screenShake < 0) {
                map.screenShake = 0;
            }
        }

        cam.position.x = map.camPosX + MathUtils.random(-map.screenShake, map.screenShake);
        cam.position.y = map.camPosY + MathUtils.random(-map.screenShake, map.screenShake);

        cam.update();
    }

    @Override
    public void renderToBuffer() {
        super.renderToBuffer();

        map.render(batch);
    }
}
