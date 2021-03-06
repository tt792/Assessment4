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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.geeselightning.zepr.game.Zepr;

/**
 * Main menu screen. <br/>
 * Assessment 3 changes:
 * <ul>
 * <li>now extends DefaultScreen</li>
 * <li>added menu button for the minigame</li>
 * </ul>
 * @author Xzytl
 * 
 */
public class MenuScreen extends DefaultScreen {
	private Stage stage;
	private Label titleLabel;
	private Label groupLabel;

	public MenuScreen(Zepr parent) {
		// Constructor builds the gui of the menu screen.
		// parent allows the MenuScreen to reference the MyGdxGame class.
		super(parent);

		// The stage is the controller which will react to inputs from the user.
		this.stage = new Stage(new ScreenViewport());
	}

	@Override
	public void show() {
		// Send any input from the user to the stage.
		Gdx.input.setInputProcessor(stage);

		// Create a table that fills the screen. Everything else will go inside this
		// table.
		Table table = new Table();
		table.setFillParent(true);
		//table.setDebug(true); // Adds borders for the table.
		stage.addActor(table);

		// hello kieran
		
		// Importing the necessary assets for the button textures.
		Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

		// Creating a title.
		titleLabel = new Label("Zombie Engineering Project", skin, "title");
		titleLabel.setColor(Color.GOLD);
		titleLabel.setFontScale(0.8f);
		groupLabel = new Label("Placeholder Games, 2019", skin);

		// Creating buttons.
		TextButton start = new TextButton("Play", skin);
		TextButton miniGame = new TextButton("UCAS Mode", skin);
		TextButton exit = new TextButton("Exit", skin);

		// Adding content to the table (screen).
		table.add(titleLabel).expandX().padBottom(64);
		table.row();
		table.add(start).padTop(16);
		table.row();
		table.add(miniGame).padTop(16);
		table.row();
		table.add(exit).padTop(16);
		table.row();
		table.add(groupLabel).padTop(80);
		

		// Defining actions for the start button.
		start.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if(parent.isZombie)
					parent.changeScreen(Zepr.ZOMBIESELECT);
				else
					parent.changeScreen(Zepr.SELECT);
			}
		});
		
		// Defining actions for the minigame button
		miniGame.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(Zepr.MINIGAME);
			}
		});

		// Defining actions for the exit button.
		exit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				Gdx.app.exit();
			}
		});

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
