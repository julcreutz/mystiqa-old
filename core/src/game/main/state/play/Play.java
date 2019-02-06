package game.main.state.play;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.MathUtils;
import game.resource.Colors;
import game.resource.SpriteSheet;
import game.main.Game;
import game.main.positionable.entity.Entity;
import game.main.positionable.entity.monster.Monster;
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
    public SpriteSheet guiStats;

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

        guiLayer = new SpriteSheet("gui/gui_layer");
        guiBar = new SpriteSheet("gui/gui_bar", 3, 1);
        guiStats = new SpriteSheet("gui/gui_stats", 4, 3);
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

        // Enemy health bars
        /*
        for (Entity e : map.entities.entities) {
            if (e instanceof Monster && e.isVulnerable) {
                batch.draw(guiStats.grab(3, 0), e.getHitbox().getCenterX() - 4, e.getHitbox().getY() + e.getHitbox().getHeight() - 2);
                batch.draw(guiStats.grab(3, 0), e.getHitbox().getCenterX() - 4 + 7, e.getHitbox().getY() + e.getHitbox().getHeight() - 2);

                batch.draw(guiStats.grab(1, 0), e.getHitbox().getCenterX() - 4 + 1, e.getHitbox().getY() + e.getHitbox().getHeight() - 2,
                        6f, 8);

                batch.draw(guiStats.grab(2, 0), e.getHitbox().getCenterX() - 4 + 1, e.getHitbox().getY() + e.getHitbox().getHeight() - 2,
                        6f * e.getHealthPercentage(), 8);

            }
        }
        */

        batch.setColor(Colors.WHITE);

        batch.draw(guiLayer.grab(0, 0), cam.position.x - Game.WIDTH * .5f, cam.position.y + Game.HEIGHT * .5f - 8, Game.WIDTH, 8);

        // Player stats
        /*
        final float offset = 1;

        // Health
        batch.draw(guiStats.grab(1, 0), cam.position.x - Game.WIDTH * .5f + 1 + offset, cam.position.y + Game.HEIGHT * .5f - 8 - offset, (map.player.getMaxHealth() - 1), 8);
        batch.draw(guiStats.grab(2, 0), cam.position.x - Game.WIDTH * .5f + 1 + offset, cam.position.y + Game.HEIGHT * .5f - 8 - offset, ((map.player.getMaxHealth() - 1)) * map.player.getHealthPercentage(), 8);
        batch.draw(guiStats.grab(3, 0), cam.position.x - Game.WIDTH * .5f + 1 + offset + (map.player.getMaxHealth() - 1), cam.position.y + Game.HEIGHT * .5f - 8 - offset);
        batch.draw(guiStats.grab(3, 0), cam.position.x - Game.WIDTH * .5f + 1 + offset, cam.position.y + Game.HEIGHT * .5f - 8 - offset);

        // Magic
        batch.draw(guiStats.grab(1, 1), cam.position.x - Game.WIDTH * .5f + 1 + offset, cam.position.y + Game.HEIGHT * .5f - 8 - 2 - offset, (map.player.maxMagic - 1), 8);
        batch.draw(guiStats.grab(2, 1), cam.position.x - Game.WIDTH * .5f + 1 + offset, cam.position.y + Game.HEIGHT * .5f - 8 - 2 - offset, ((map.player.maxMagic - 1)) * map.player.getMagicPercentage(), 8);
        batch.draw(guiStats.grab(3, 1), cam.position.x - Game.WIDTH * .5f + 1 + offset + (map.player.maxMagic - 1), cam.position.y + Game.HEIGHT * .5f - 8 - 2 - offset);
        batch.draw(guiStats.grab(3, 1), cam.position.x - Game.WIDTH * .5f + 1 + offset, cam.position.y + Game.HEIGHT * .5f - 8 - 2 - offset);
        */

        /*
        // Experience
        batch.draw(guiStats.grab(1, 2), cam.position.x - Game.WIDTH * .5f + 1 + offset, cam.position.y - Game.HEIGHT * .5f - 1 - offset, 75, 8);
        batch.draw(guiStats.grab(2, 2), cam.position.x - Game.WIDTH * .5f + 1 + offset, cam.position.y - Game.HEIGHT * .5f - 1 - offset, (75) * map.player.getExperiencePercentage(), 8);
        batch.draw(guiStats.grab(3, 2), cam.position.x - Game.WIDTH * .5f + 1 + offset + 75, cam.position.y - Game.HEIGHT * .5f - 1 - offset);
        batch.draw(guiStats.grab(3, 2), cam.position.x - Game.WIDTH * .5f + 1 + offset, cam.position.y - Game.HEIGHT * .5f - 1 - offset);
        */

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
