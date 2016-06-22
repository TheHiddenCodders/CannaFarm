package com.mygdx.scene.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.scene.CannaFarm;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration window = new LwjglApplicationConfiguration();
		window.title = "CannaFarm";
		window.width = 800;
		window.height = 480;
		
		new LwjglApplication(new CannaFarm(), window);
	}
}
