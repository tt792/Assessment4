package com.geeselightning.zepr.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.geeselightning.zepr.screens.*;

/**
 * Main game class. <br/>
 * Assessment 3 changes:
 * <ul>
 * <li>replaced different level screens with GameScreen</li>
 * <li>added minigame screen</li>
 * </ul>
 * Assessment 4 changes:
 * <ul>
 * <li>Added Zombie maps</li>
 * <li></li>
 * </ul>
 * @author Xzytl
 *
 */
public class Zepr extends Game {

	private MenuScreen menuScreen;
	private LoadingScreen loadingScreen;
	private ZombieSelectScreen zombieSelectScreen;
	private SelectCharacterScreen selectLevelScreen;
	private GameScreen gameScreen;
	private MiniGameScreen miniGameScreen;
	
	public static boolean devMode;
	public boolean isZombie = false;
	
	public final static int MENU = 0;
	public final static int LOADING = 1;
	public final static int ZOMBIESELECT = 2;
	public final static int SELECT = 3;
	public final static int GAME = 4;
	public final static int ZOMBIE = 5;
	public final static int ZOMBIE2 = 6;
	public final static int ZOMBIE3 = 7;
	public final static int LEVEL_COMPLETE = 8;
	public final static int MINIGAME = 9;
	
	// Assessment 3: added dev mode
	public Zepr(boolean devMode) {
		super();
		Zepr.devMode = devMode;
	}

	/**
	 * Changes the active screen to a predefined screen in Zepr.
	 * @param screen	the new active screen
	 * </br>
	 * Assessment 4:
	 * (F11) Changed to include the different levels for once the player is a Zombie
	 */
	public void changeScreen(int screen) {
		
		Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
		
		TextButton nextStage = new TextButton("next stage", skin, "arcade");
		TextButton zombieLevel = new TextButton("Continue", skin);
		TextButton zombie2 = new TextButton("Continue", skin);
		TextButton zombie3 = new TextButton("Continue", skin);
		
		nextStage.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(SELECT);
				dispose();
			}
		});
		zombieLevel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				GameManager.instance.setLevelProgress(10);
				changeScreen(ZOMBIESELECT);
				dispose();
			}
		});
		zombie2.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(ZOMBIESELECT);
				dispose();
			}
		});
		zombie3.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				changeScreen(SELECT);
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
				if (loadingScreen == null) selectLevelScreen = new SelectCharacterScreen(this);
				this.setScreen(selectLevelScreen);
				break;
			case ZOMBIESELECT:
				if (loadingScreen == null) zombieSelectScreen = new ZombieSelectScreen(this);
				this.setScreen(zombieSelectScreen);
				break;
			case GAME:
				if (gameScreen == null) gameScreen = new GameScreen(this);
				this.setScreen(gameScreen);
				break;
			case ZOMBIE:
				this.setScreen(new TextScreen(this, "You passed out", "You awaken in an unknown location", zombieLevel));
				break;
			case ZOMBIE2:
				this.setScreen(new TextScreen(this, "Stage complete", "loading next map...", zombie2));
				break;
			case ZOMBIE3:
				this.setScreen(new TextScreen(this, "You are cured", "You are now a human again", zombie3));
				break;
			case LEVEL_COMPLETE:
				this.setScreen(new TextScreen(this, "Stage " + SelectCharacterScreen.getStageName() + " complete!", "loading next map...", nextStage));
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