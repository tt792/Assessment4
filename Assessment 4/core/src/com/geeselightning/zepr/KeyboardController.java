package com.geeselightning.zepr;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

/**
 * InputProcessor that handles basic input events. <br/>
 * Assessment 3 changes:
 * <ul>
 * <li>no longer directly references player - allows generic use</li>
 * <li>allows use of arrow keys in addition to WASD</li>
 * </ul>
 * @author Xzytl
 *
 */
public class KeyboardController implements InputProcessor {

	public boolean left, right, up, down;

	public boolean isMouse1Down, isMouse2Down, isMouse3Down;

	public boolean isDragged;

	public Vector2 mousePosition = new Vector2(0, 0);

	@Override
	public boolean keyDown(int keycode) {
		boolean keyProcessed = false;
		switch (keycode) {
		case Keys.LEFT:
		case Keys.A:
			left = true;
			keyProcessed = true;
			break;
		case Keys.RIGHT:
		case Keys.D:
			right = true;
			keyProcessed = true;
			break;
		case Keys.UP:
		case Keys.W:
			up = true;
			keyProcessed = true;
			break;
		case Keys.DOWN:
		case Keys.S:
			down = true;
			keyProcessed = true;
			break;
		}
		return keyProcessed;
	}

	@Override
	public boolean keyUp(int keycode) {
		boolean keyProcessed = false;
		switch (keycode) {
		case Keys.LEFT:
		case Keys.A:
			left = false;
			keyProcessed = true;
			break;
		case Keys.RIGHT:
		case Keys.D:
			right = false;
			keyProcessed = true;
			break;
		case Keys.UP:
		case Keys.W:
			up = false;
			keyProcessed = true;
			break;
		case Keys.DOWN:
		case Keys.S:
			down = false;
			keyProcessed = true;
			break;
		}
		return keyProcessed;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		boolean buttonProcessed = false;
		if (button == 0) {
			isMouse1Down = true;
			buttonProcessed = true;
		} 
		if (button == 1) {
			isMouse2Down = true;
			buttonProcessed = true;
		}
		if (button == 2) {
			isMouse3Down = true;
			buttonProcessed = true;
		}
		mousePosition.x = screenX;
		mousePosition.y = screenY;
		return buttonProcessed;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		boolean buttonProcessed = false;
		if (button == 0) {
			isMouse1Down = false;
			buttonProcessed = true;
		} 
		if (button == 1) {
			isMouse2Down = false;
			buttonProcessed = true;
		}
		if (button == 2) {
			isMouse3Down = false;
			buttonProcessed = true;
		}
		mousePosition.x = screenX;
		mousePosition.y = screenY;
		return buttonProcessed;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		isDragged = true;
		mousePosition.x = screenX;
		mousePosition.y = screenY;
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		mousePosition.x = screenX;
		mousePosition.y = screenY;
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
