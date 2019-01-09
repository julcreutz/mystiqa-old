package game.main.state.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
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

        nextMap = Game.MAPS.load("Dungeon");
        nextMap.generate();
        nextMap.placePlayer();
    }

    @Override
    public void update(Game g) {
        super.update(g);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            nextMap = Game.MAPS.load("Dungeon");
            nextMap.generate();
            nextMap.placePlayer();
        }

        if (nextMap != null) {
            map = nextMap;
            map.positionCamera();
            nextMap = null;
        }

        if (map.screenShake == 0) {
            map.update(this);
        } else {
            map.screenShake -= Game.getDelta() * 10f;

            if (map.screenShake < 0) {
                map.screenShake = 0;
            }
        }

        cam.position.x = map.camPosX;
        cam.position.y = map.camPosY;

        if (map.screenShake > 0) {
            cam.position.x += MathUtils.random(-1, 1);
            cam.position.y += MathUtils.random(-1, 1);
        }

        cam.update();
    }

    @Override
    public void renderToBuffer() {
        super.renderToBuffer();

        map.render(batch);
    }
}
