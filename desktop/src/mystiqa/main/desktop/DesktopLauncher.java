package mystiqa.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import mystiqa.main.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		final int scale = 12;

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Mystiqa";
		config.width = 128 * scale;
		config.height = 72 * scale;
		config.backgroundFPS = 0;
		config.foregroundFPS = 0;
		config.vSyncEnabled = true;
		config.resizable = false;
		new LwjglApplication(new Game(), config);
	}
}
