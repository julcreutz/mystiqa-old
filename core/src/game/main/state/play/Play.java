package game.main.state.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import game.main.Game;
import game.main.state.GameState;
import game.main.state.play.map.Map;
import game.main.state.play.map.dungeon.Cave;

public class Play extends GameState {
    public Map map;
    public Map nextMap;

    public FrameBuffer game;
    public FrameBuffer lighting;

    @Override
    public void create() {
        super.create();

        game = createFrameBuffer();
        lighting = createFrameBuffer();

        nextMap = new Cave();
        nextMap.generate();
    }

    @Override
    public void update(Game g) {
        super.update(g);

        if (Gdx.input.isKeyJustPressed(Input.Keys.R)) {
            nextMap = new Cave();
            nextMap.generate();
        }

        if (nextMap != null) {
            map = nextMap;
            map.play = this;
            map.positionCamera();
            nextMap = null;
        }

        if (map.screenShake == 0) {
            map.update();
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
    public void render() {
        renderGame();
        renderLighting();

        super.render();
    }

    @Override
    public void renderToBuffer() {
        super.renderToBuffer();

        renderBuffer(game);

        batch.setShader(Game.SHADERS.load("lighting"));
        renderBuffer(lighting);
        batch.setShader(null);
    }

    public void renderGame() {
        game.begin();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();
        map.render(batch);
        batch.end();

        game.end();
    }

    public void renderLighting() {
        lighting.begin();

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        batch.setColor(1, 1, 1, 1);
        batch.setColor(1, 1, 1, 1);

        batch.end();

        lighting.end();
    }

    @Override
    public void dispose() {
        super.dispose();

        game.dispose();
        lighting.dispose();
    }
}
