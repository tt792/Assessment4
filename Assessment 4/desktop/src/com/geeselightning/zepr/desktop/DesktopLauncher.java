package com.geeselightning.zepr.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.geeselightning.zepr.game.Zepr;

public class DesktopLauncher {
	public static void main (String[] args) {
		boolean devMode = false;
		for(String arg : args) {
			if (arg.equals("--dev")) {
				devMode = true;
			}

		}
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.resizable = false;
//		config.width = 1280;
//		config.height = 720;
		config.width = 1600;
		config.height = 900;
		new LwjglApplication(new Zepr(devMode), config);
	}
}
