package mystiqa.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import mystiqa.world_generation.WorldGenerator;

public class WorldGeneratorLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "World Generator";
        config.width = 1280;
        config.height = 720;
        config.backgroundFPS = 0;
        config.foregroundFPS = 0;
        config.vSyncEnabled = true;
        config.resizable = false;

        new LwjglApplication(new WorldGenerator(), config);
    }
}
