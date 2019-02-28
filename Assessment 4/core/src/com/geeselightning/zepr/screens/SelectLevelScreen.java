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
public class SelectLevelScreen extends DefaultScreen {

	private Stage stage;
	private Label stageDescription;
	private Label characterDescription;
	// Defines whether a level has been selected
	private boolean levelSet;
	// Defines whether a player class has been selected
	private boolean playerSet;
	
	private TextButton town;
	private TextButton halifax;
	private TextButton centralHall;
	private TextButton courtyard;
	private TextButton library;
	private TextButton ronCooke;
	
	private final String townDescription = "You wake up hungover in town to discover there is a zombie apocalypse.";
	private final String halifaxDescription = "You need to get your laptop with the work on it from your accommodation.";
	private final String centralHallDescription = "For no readily apparent reason, you decide central hall would be a nice place to relax.";
	private final String courtyardDescription = "You should go to Courtyard and get some breakfast.";
	private final String libraryDescription = "Surely no zombie would dare defile the sacred silent space of the library? Yeah, right.";
	private final String ronCookeDescription = "Are you going to allow a little zombie plague to stop you getting all 9250 pounds worth of your education? Time for lectures.";
	
	private GameManager gameManager;

	public SelectLevelScreen(Zepr parent) {
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
		Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

		/* Top menu bar buttons */
		TextButton save = new TextButton("Save", skin);
		TextButton load = new TextButton("Load", skin);
		TextButton back = new TextButton("Back", skin);
		
		/* Level selection buttons */
		town = new TextButton("Town", skin);
		halifax = new TextButton("Halifax", skin);
		centralHall = new TextButton("Central Hall", skin);
		courtyard = new TextButton("Courtyard", skin);
		library = new TextButton("Library", skin);
		ronCooke = new TextButton("Ron Cooke", skin);

		/* Character selection buttons */
		TextButton nerdy = new TextButton("Nerdy", skin);
		TextButton sporty = new TextButton("Sporty", skin);
		TextButton heavy = new TextButton("Heavy", skin);

		/* Play button */
		TextButton play = new TextButton("Play", skin);

		/* Level descriptions */
		Label title = new Label("Choose a stage and character.", skin, "subtitle");
		//final String lockedDescription = "This stage is locked until you complete the previous one.";
		final String defaultDescription = "Select a stage from the buttons above.";
		stageDescription = new Label(defaultDescription, skin);
		stageDescription.setWrap(true);
		stageDescription.setWidth(100);
		stageDescription.setAlignment(Align.center);

		/* Character descriptions */
		final String nerdyDescription = "Construct a mech suit for yourself so you can take more hits.";
		final String sportyDescripton = "Work out so you run faster.";
		final String heavyDescription = "Heavier and stronger, with a powerful pack-a-punch";
		final String defaultCharacterDescription = "Select a type of student from the buttons above.";
		characterDescription = new Label(defaultCharacterDescription, skin);
		characterDescription.setWrap(true);
		characterDescription.setWidth(100);
		characterDescription.setAlignment(Align.center);

		// Adding menu bar.
		Table menuBar = new Table();
		menuBar.setFillParent(true);
		// menuBar.setDebug(true); // Adds borders for the table.
		stage.addActor(menuBar);

		menuBar.top().left();
		menuBar.row();
		menuBar.add(back).pad(10);
		menuBar.add(save).pad(10);
		menuBar.add(load).pad(10);
		// Adding stage selector buttons.
		Table stageSelect = new Table();
		stageSelect.setFillParent(true);
		// stageSelect.setDebug(true); // Adds borders for the table.
		stage.addActor(stageSelect);

		stageSelect.center();

		stageSelect.row();
		stageSelect.add(title).colspan(3);

		stageSelect.row().pad(50, 0, 100, 0);
		stageSelect.add(town).pad(10);
		stageSelect.add(halifax).pad(10);
		stageSelect.add(centralHall).pad(10).row();
		stageSelect.add(courtyard).pad(10);
		stageSelect.add(library).pad(10);
		stageSelect.add(ronCooke).pad(10);

		stageSelect.row();
		stageSelect.add(stageDescription).width(1000f).colspan(3);

		// Adding select character Buttons
		stageSelect.row().center();
		stageSelect.add(nerdy).pad(10);
		stageSelect.add(sporty).pad(10);
		stageSelect.add(heavy).pad(10);

		stageSelect.row().center();
		stageSelect.add(characterDescription).width(1000f).colspan(3);

		// Adding play button at the bottom.
		Table bottomTable = new Table();
		bottomTable.setFillParent(true);
		// bottomTable.setDebug(true); // Adds borders for the table.
		stage.addActor(bottomTable);

		bottomTable.bottom();
		bottomTable.add(play).pad(10).center();

		/**
		 * Button event listeners
		 */
		
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
			}
		});

		/**
		 * Defines action for the play button.
		 */
		play.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (levelSet && playerSet) {
					parent.changeScreen(Zepr.GAME);
				}
			}
		});
		
		setLevelSelectionHandlers();

	}
	
	/**
	 * Enables/disables level selection buttons depending on whether the user has unlocked it.
	 * Assessment 3: moved the following code from the show method so that it can
	 * be called independently to allow dynamic button enabling/disabling (mainly for loading
	 * functionality).
	 */
	private void setLevelSelectionHandlers() {
		/**
		 * Defines action for the town level selection button.
		 */
		town.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				stageDescription.setText(townDescription);
				gameManager.setLocation(Level.Location.TOWN);
				levelSet = true;
			}
		});

		/**
		 * Defines action for the halifax level selection button.
		 */
		if (gameManager.getLevelProgress() < 1) {
			disabledButtonStyle(halifax);
		} else {
			enabledButtonStyle(halifax);
			// Defining actions for the halifax button.
			halifax.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					stageDescription.setText(halifaxDescription);
					gameManager.setLocation(Level.Location.HALIFAX);
					levelSet = true;
				}
			});
		}

		/**
		 * Defines action for the central hall level selection button.
		 */
		if (gameManager.getLevelProgress() < 2) {
			disabledButtonStyle(centralHall);
		} else {
			enabledButtonStyle(centralHall);
			centralHall.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					stageDescription.setText(centralHallDescription);
					gameManager.setLocation(Level.Location.CENTRALHALL);
					levelSet = true;
				}
			});
		}
		
		/**
		 * Defines action for the courtyard level selection button.
		 */
		if (gameManager.getLevelProgress() < 3) {
			disabledButtonStyle(courtyard);
		} else {
			enabledButtonStyle(courtyard);
			// Defining actions for the courtyard button.
			courtyard.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					stageDescription.setText(courtyardDescription);
					gameManager.setLocation(Level.Location.COURTYARD);
					levelSet = true;
				}
			});
		}
		
		/**
		 * Defines action for the library level selection button.
		 */
		if (gameManager.getLevelProgress() < 4) {
			disabledButtonStyle(library);
		} else {
			enabledButtonStyle(library);
			library.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					stageDescription.setText(libraryDescription);
					gameManager.setLocation(Level.Location.LIBRARY);
					levelSet = true;
				}
			});
		}
		
		/**
		 * Defines action for the Ron Cooke level selection button.
		 */
		if (gameManager.getLevelProgress() < 5) {
			disabledButtonStyle(ronCooke);
		} else {
			enabledButtonStyle(ronCooke);
			ronCooke.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					stageDescription.setText(ronCookeDescription);
					gameManager.setLocation(Level.Location.RONCOOKE);
					levelSet = true;
				}
			});
		}
		
	}
	
	// Assessment 3: convenience method to set a button to the disabled style.
	private void disabledButtonStyle(TextButton button) {
		button.setColor(Color.DARK_GRAY);
		button.getLabel().setColor(Color.DARK_GRAY);
	}
	
	// Assessment 3: convenience method to set a button to the enabled style.
	private void enabledButtonStyle(TextButton button) {
		button.setColor(Color.WHITE);
		button.getLabel().setColor(Color.WHITE);
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
