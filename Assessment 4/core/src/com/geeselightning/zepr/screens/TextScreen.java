package com.geeselightning.zepr.screens;

import com.badlogic.gdx.Gdx;
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
 * Generic screen for displaying text. <br/>
 * Assessment 3 changes:
 * <ul>
 * <li>class now extends DefaultScreen</li>
 * <li>now allows subtitle text</li>
 * </ul>
 * @author Xzytl
 *
 */
public class TextScreen extends DefaultScreen {

	private Label titleText;
	private Label subtitleText;
	private Zepr parent;
	private Stage stage;
	private Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

	public TextScreen(Zepr parent, String title, String subtitle) {
		super(parent);

		this.parent = parent;
		
		titleText = new Label(title, skin);
		subtitleText = new Label(subtitle, skin);

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
		stage.addActor(table);
		table.center();

		TextButton back = new TextButton("Back", skin);

		// Adding content to the table (screen).
		table.add(titleText);
		table.row().pad(30);
		table.add(subtitleText).row();
		table.add(back);

		// Defining actions for the preferences button.
		back.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				parent.changeScreen(Zepr.SELECT);
				dispose();
			}
		});
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		// Draws the stage.
		this.stage.act();
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
