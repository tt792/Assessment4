package com.geeselightning.zepr.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.geeselightning.zepr.screens.*;
import com.geeselightning.zepr.world.Level.Location;

/**
 * Main game class. <br/>
 * Assessment 3 changes:
 * <ul>
 * <li>replaced different level screens with GameScreen</li>
 * <li>added minigame screen</li>
 * </ul>
 * @author Xzytl
 *
 */
public class Zepr extends Game {

	private MenuScreen menuScreen;
	private LoadingScreen loadingScreen;
	private ZombieSelectScreen zombieSelectScreen;
	private SelectLevelScreen selectLevelScreen;
	private GameScreen gameScreen;
	private MiniGameScreen miniGameScreen;
	
	public static boolean devMode = true;

	public final static int MENU = 0;
	public final static int LOADING = 1;
	public final static int ZOMBIESELECT = 2;
	public final static int SELECT = 3;
	public final static int GAME = 4;
	public final static int ZOMBIE = 5;
	public final static int LEVEL_COMPLETE = 6;
	public final static int MINIGAME = 7;
	
	// Assessment 3: added dev mode
	public Zepr(boolean devMode) {
		super();
		Zepr.devMode = true;
	}

	/**
	 * Changes the active screen to a predefined screen in Zepr.
	 * @param screen	the new active screen
	 */
	public void changeScreen(int screen) {
		
		Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
		
		TextButton back = new TextButton("Back", skin);
		TextButton zombieLevel = new TextButton("Continue", skin);
		
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(Zepr.SELECT);
				dispose();
			}
		});
		zombieLevel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(ZOMBIESELECT);
				dispose();
			}
		});
		
		switch(screen) {
			case MENU:
				if (menuScreen == null) menuScreen = new MenuScreen(this);
				this.setScreen(menuScreen);
				break;
			case LOADING:
				if (loadingScreen == null) loadingScreen = new LoadingScreen(this);
				this.setScreen(loadingScreen);
				break;
			case SELECT:
				if (selectLevelScreen == null) selectLevelScreen = new SelectLevelScreen(this);
				this.setScreen(selectLevelScreen);
				break;
			case ZOMBIESELECT:
				if (zombieSelectScreen == null) zombieSelectScreen = new ZombieSelectScreen(this);
				this.setScreen(zombieSelectScreen);
				break;
			case GAME:
				if (gameScreen == null) gameScreen = new GameScreen(this);
				this.setScreen(gameScreen);
				break;
			case ZOMBIE:
				this.setScreen(new TextScreen(this, "You passed out", "You awaken in an unknown location", zombieLevel));
				break;
			case LEVEL_COMPLETE:
				this.setScreen(new TextScreen(this, "Level complete", "You have successfully completed the level", back));
				break;
			case MINIGAME:
				if (miniGameScreen == null) miniGameScreen = new MiniGameScreen(this);
				this.setScreen(miniGameScreen);
				break;
		}
	}

	@Override
	public void create() {
		changeScreen(MENU);
	}
	
}