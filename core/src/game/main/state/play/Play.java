package game.main.state.play;

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

        if (nextMap != null) {
            map = nextMap;
            map.positionCam();
            nextMap = null;
        }

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
