package com.geeselightning.zepr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.geeselightning.zepr.entities.Player;
import com.geeselightning.zepr.game.GameManager;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.world.Level;

/**
 * Screen that displays level and player class options. <br/>
 * Assessment 3 changes:
 * <ul>
 * <li>class now extends DefaultScreen</li>
 * <li>save/load functionality fully implemented</li>
 * <li>three additional levels added</li>
 * <li>level selection is now passed to GameManager for loading, rather than each level being a 
 * 		different screen to switch to.</li>
 * </ul>
 * @author Xzytl
 *
 */
public class SelectCharacterScreen extends DefaultScreen {

	private Stage stage;
	private Label characterDescription;
	public static String stageName = "1: Town";
	
	Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
	
	Label currentStageLabel = new Label("Stage " + stageName, skin, "title");
	
	/* Character selection buttons */
	TextButton nerdy = new TextButton("Nerdy", skin);
	TextButton sporty = new TextButton("Sporty", skin);
	TextButton heavy = new TextButton("Heavy", skin);
	
	
	/* Start the game button */
	TextButton play = new TextButton("Start game!", skin, "arcade");
	
	// Defines whether a level has been selected
	// Defines whether a player class has been selected
	private boolean playerSet;
	
	private GameManager gameManager;

	public SelectCharacterScreen(Zepr parent) {
		super(parent);

		// The stage is the controller which will react to inputs from the user.
		this.stage = new Stage(new ScreenViewport());
		
		gameManager = GameManager.getInstance(parent);
	}

	@Override
	public void show() {
		// Send any input from the user to the stage.
		Gdx.input.setInputProcessor(stage);

		// Importing the necessary assets for the button textures.
		

		/* Top menu bar buttons */
		TextButton save = new TextButton("save", skin);
		TextButton load = new TextButton("load", skin);
		TextButton back = new TextButton("<-", skin);
		
		/* Character descriptions */
		final String nerdyDescription = "I'm so smart I built myself a protective suit";
		final String sportyDescripton = "I run super fast. That's kind of it though.";
		final String heavyDescription = "Fight me. Coz' I WILL win.";
		
		characterDescription = new Label("Choose your character:", skin);
		characterDescription.setWrap(true);
		characterDescription.setWidth(100);
		characterDescription.setAlignment(Align.center);

		// Adding menu bar.
		Table backSaveLoad = new Table();
		backSaveLoad.setFillParent(true);
		// backSaveLoad.setDebug(true); // Adds borders for the table.
		stage.addActor(backSaveLoad);

		backSaveLoad.top();
		backSaveLoad.row();
		backSaveLoad.add(back).padRight(800).padTop(10);
		backSaveLoad.add(save).padRight(10).padTop(10);
		backSaveLoad.add(load).padTop(10);
		
		Table characterSelect = new Table();
		characterSelect.setFillParent(true);
		// characterSelect.setDebug(true); // Adds borders for the table.
		stage.addActor(characterSelect);

		/* Adding character selector buttons and NOT "level" selector buttons
		   BECAUSE WE'RE SMART ENOUGH TO CODE AUTOMATIC STAGE CHOOSING OK?!
		   WHY WOULD YOU WANNA DO THE SAME LEVEL TWICE ANYWAY
		*/
		
		characterSelect.center();
		
		/* Stage number label */
		characterSelect.add(currentStageLabel).colspan(3).padBottom(10);
		characterSelect.row();
		characterSelect.add(characterDescription).width(1000f).colspan(3);
		characterSelect.row();
		
		characterSelect.row().center();
		characterSelect.add(nerdy).pad(10);
		nerdy.setColor(Color.BLACK);
		characterSelect.add(sporty).pad(10);
		sporty.setColor(Color.BLACK);
		characterSelect.add(heavy).pad(10);
		heavy.setColor(Color.BLACK);

		characterSelect.row().center();

		// Adding play button at the bottom.
		Table bottomTable = new Table();
		bottomTable.setFillParent(true);
		//bottomTable.setDebug(true); // Adds borders for the table.
		stage.addActor(bottomTable);

		bottomTable.bottom();
		bottomTable.add(play).pad(10).center();
		
		/**
		 * Defines action for the save button.
		 */
		save.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				System.out.println("Level progress saved: " + gameManager.getLevelProgress());
				gameManager.getPrefs().putInteger("levelProgress", gameManager.getLevelProgress());
				gameManager.getPrefs().flush();
			}
		});
		
		/**
		 * Defines action for the load button.
		 */
		load.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				int levelProgress = gameManager.getPrefs().getInteger("levelProgress", 0);
				System.out.println("Level progress loaded: " + levelProgress);
				if (levelProgress > gameManager.getLevelProgress()) {
					gameManager.setLevelProgress(levelProgress);
					setLevelSelectionHandlers();
				}
			}
		});

		/**
		 * Defines action for the back button.
		 */
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(Zepr.MENU);
			}
		});

		/**
		 * Defines action for the nerdy character selection button.
		 */
		nerdy.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				characterDescription.setText(nerdyDescription);
				gameManager.setPlayerType(Player.Type.NERDY);
				playerSet = true;
				nerdy.setColor(Color.YELLOW);
				sporty.setColor(Color.BLACK);
				heavy.setColor(Color.BLACK);
			}
		});
		
		/**
		 * Defines action for the sporty character selection button.
		 */
		sporty.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				characterDescription.setText(sportyDescripton);
				gameManager.setPlayerType(Player.Type.SPORTY);
				playerSet = true;
				nerdy.setColor(Color.BLACK);
				sporty.setColor(Color.YELLOW);
				heavy.setColor(Color.BLACK);
			}
		});
		
		/**
		 * Defines action for the heavy character selection button.
		 */
		heavy.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				characterDescription.setText(heavyDescription);
				gameManager.setPlayerType(Player.Type.HEAVY);
				playerSet = true;
				nerdy.setColor(Color.BLACK);
				sporty.setColor(Color.BLACK);
				heavy.setColor(Color.YELLOW);
			}
		});

		/**
		 * Defines action for the play button.
		 */
		play.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (playerSet) {
					parent.changeScreen(Zepr.GAME);
				}
			}
		});
		
		setLevelSelectionHandlers();

	}
	
	/**
	 * Updates the text for what stage the player is on, as the player progresses the game
	 * Assessment 3: moved the following code from the show method so that it can
	 * be called independently to allow dynamic string updating (mainly for loading
	 * functionality).
	 * @return 
	 */
	
	private void setLevelSelectionHandlers() {
		/**
		 * Defines action for the town level selection button.
		 */

		if (gameManager.getLevelProgress() == 0) {
			gameManager.setLocation(Level.Location.TOWN);
		} 
		else if (gameManager.getLevelProgress() == 1) {
			gameManager.setLocation(Level.Location.HALIFAX);
			stageName = "2: Halifax";
			currentStageLabel.setText("Stage " + stageName);
			currentStageLabel.setColor(Color.CYAN);
		}
		else if (gameManager.getLevelProgress() == 2) {
			gameManager.setLocation(Level.Location.CENTRALHALL);
			stageName = "3: Central Hall";
			currentStageLabel.setText("Stage " + stageName);
			currentStageLabel.setColor(Color.SALMON);
			}
		else if (gameManager.getLevelProgress() == 3) {
			gameManager.setLocation(Level.Location.COURTYARD);
			stageName = "4: Courtyard";
			currentStageLabel.setText("Stage " + stageName);
			currentStageLabel.setColor(Color.BROWN);
			}
		else if (gameManager.getLevelProgress() == 4) {
			gameManager.setLocation(Level.Location.LIBRARY);
			stageName = "5: JB Morrell";
			currentStageLabel.setText("Stage " + stageName);
			currentStageLabel.setColor(Color.SLATE);
			}
		else if (gameManager.getLevelProgress() == 5) {
			gameManager.setLocation(Level.Location.RONCOOKE);
			stageName = "Ron Cooke Hub";
			currentStageLabel.setText("Final Stage" + stageName);
			currentStageLabel.setColor(Color.GOLD);
			}
		else if (gameManager.getLevelProgress() > 5) {
			currentStageLabel.setText("Congratulations!");
			characterDescription.setText("You have won the game!");
			nerdy.remove();
			heavy.remove();
			sporty.remove();
			play.remove();
		}		
		 
	}
	
	/* Assessment 4: convenience method(?), returns which stage the user completes
	 * to update the "you have completed a stage!" screen
	 */
	public static char getStageName() {
		return stageName.charAt(0);
	}
	
	@Override
	public void render(float delta) {
		super.render(delta);

		// Draws the stage.
		this.stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		this.stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// Update the screen when the window resolution is changed.
		this.stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void pause() {
		// TODO Auto-generated method stub
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
	}
	
	@Override
	public void dispose() {
		// Dispose of assets when they are no longer needed.
		stage.dispose();
	}

}
