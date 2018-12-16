package game.main.site;

import game.main.Game;
import game.main.GameState;

public class SiteState extends GameState {
    public SiteData data;

    @Override
    public void update(Game g) {
        data.update(this);
    }

    @Override
    public void renderToBuffer() {
        data.render(batch);
    }
}
