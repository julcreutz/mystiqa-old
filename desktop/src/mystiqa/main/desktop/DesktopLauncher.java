package mystiqa.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import mystiqa.main.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		final int scale = 6;

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Mystiqa";
		config.width = 256 * scale;
		config.height = 144 * scale;
		config.backgroundFPS = 0;
		config.foregroundFPS = 0;
		config.vSyncEnabled = true;
		config.resizable = false;
		new LwjglApplication(new Game(), config);
	}
}
