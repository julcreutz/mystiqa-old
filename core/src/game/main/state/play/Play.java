package game.main.state.play;

import game.main.Game;
import game.main.state.GameState;
import game.main.state.play.map.Map;

public class Play extends GameState {
    public Map map;

    @Override
    public void create() {
        super.create();

        map = Game.MAPS.load("Overworld");
        map.generate();
    }

    @Override
    public void update(Game g) {
        super.update(g);

        cam.position.x = map.camPosX;
        cam.position.y = map.camPosY;
        cam.update();

        map.update(this);
    }

    @Override
    public void renderToBuffer() {
        super.renderToBuffer();

        map.render(batch);
    }
}
