package game.main.region;

import game.main.Game;
import game.main.GameState;

public class Region extends GameState {
    public RegionData data;

    @Override
    public void update(Game g) {
        data.update(this);
    }

    @Override
    public void renderToBuffer() {
        data.render(batch);
    }
}
