package game.main.state.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import game.Colors;
import game.SpriteSheet;
import game.main.Game;
import game.main.state.GameState;
import game.main.state.play.map.Map;
import game.main.state.play.map.Cave;

public class Play extends GameState {
    public Map map;
    public Map nextMap;

    public FrameBuffer game;
    public FrameBuffer lighting;
    public FrameBuffer gui;

    public SpriteSheet guiLayer;
    public SpriteSheet guiBar;

    public String message;
    public float messageTime;

    @Override
    public void create() {
        super.create();

        game = createFrameBuffer();
        lighting = createFrameBuffer();
        gui = createFrameBuffer();

        nextMap = new Cave();
        nextMap.generate();

        guiLayer = new SpriteSheet("gui_layer");
        guiBar = new SpriteSheet("gui_bar");
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

        if (messageTime > 0) {
            messageTime -= Game.getDelta();

            if (messageTime < 0) {
                messageTime = 0;
                message = null;
            }
        }
    }

    @Override
    public void render() {
        renderGame();
        renderLighting();
        renderGUI();

        super.render();
    }

    @Override
    public void renderToBuffer() {
        super.renderToBuffer();

        renderBuffer(game);

        batch.setShader(Game.SHADERS.load("lighting"));
        renderBuffer(lighting);
        batch.setShader(null);

        renderBuffer(gui);
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
        batch.end();

        lighting.end();
    }

    public void renderGUI() {
        gui.begin();

        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        // GUI
        batch.draw(guiLayer.grab(0, 0),
                cam.position.x - Game.WIDTH * .5f, cam.position.y + Game.HEIGHT * .5f - 8, Game.WIDTH, 8);

        if (message != null) {
            write(message, cam.position.x, cam.position.y + Game.HEIGHT * .5f - 8, true);
        }

        // Health
        batch.setColor(Colors.RED);
        String health = "" + MathUtils.round(map.player.health);

        write(health, cam.position.x - Game.WIDTH * .5f, cam.position.y + Game.HEIGHT * .5f - 4, false);
        batch.draw(guiBar.grab(0, 0), cam.position.x - Game.WIDTH * .5f + health.length() * 4, cam.position.y + Game.HEIGHT * .5f - 4,
                (32 - health.length() * 4) * map.player.getHealthPercentage(), 4);

        // Level and experience
        batch.setColor(Colors.GREEN);
        String level = "" + (map.player.level + 1);

        write(level, cam.position.x, cam.position.y + Game.HEIGHT * .5f - 4, false);
        batch.draw(guiBar.grab(0, 0), cam.position.x + level.length() * 4, cam.position.y + Game.HEIGHT * .5f - 4,
                (32 - level.length() * 4) * map.player.getExperiencePercentage(), 4);

        batch.setColor(Colors.WHITE);

        batch.end();

        gui.end();
    }

    @Override
    public void dispose() {
        super.dispose();

        game.dispose();
        lighting.dispose();
        gui.dispose();
    }

    public void setMessage(String message) {
        this.message = message;
        messageTime = 2;
    }
}
