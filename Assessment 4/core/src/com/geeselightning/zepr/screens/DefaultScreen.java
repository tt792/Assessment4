package com.geeselightning.zepr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.geeselightning.zepr.game.Zepr;

/**
 * Represents a basic implementation of {@link Screen}. Ensures all child classes have a reference
 * to an instance of Zepr (since all screens need the ability to switch to other screens), and
 * provides a default render method that all child classes will require (though may well need to
 * extend).
 * Implemented in assessment 3.
 * @author Xzytl
 *
 */
public abstract class DefaultScreen implements Screen {
	
	public final Zepr parent;

	public DefaultScreen(Zepr parent) {
		this.parent = parent;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

}
