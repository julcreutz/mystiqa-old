package game.main.state.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
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

    public FrameBuffer game;
    public FrameBuffer lighting;

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

        batch.setShader(Game.SHADERS.load("Lighting").shader);
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

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        batch.setColor(1, 1, 1, 1);
        batch.draw(Game.SPRITE_SHEETS.load("LightCircle").sheet[0][0],
                MathUtils.round(map.player.x + 4 - 128), MathUtils.round(map.player.y + 4 - 128),
                128, 128, 256, 256, 0.125f, 0.125f, 0);
        batch.setColor(1, 1, 1, 1);

        batch.end();

        lighting.end();
    }

    @Override
    public void resize(int w, int h) {
        super.resize(w, h);

        game = createFrameBuffer();
        lighting = createFrameBuffer();
    }

    @Override
    public void dispose() {
        super.dispose();

        lighting.dispose();
    }
}
