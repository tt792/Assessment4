package com.geeselightning.zepr.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.geeselightning.zepr.entities.PowerUp;
import com.geeselightning.zepr.game.Zepr;

/**
 * Game overlay stage that relays important information such as health and
 * progress. This class replaces the code in the original Level class that
 * performed the same function. Implemented in assessment 3.
 * @author Xzytl
 * 
 */
public class Hud implements Disposable {

	public Stage stage;
	private ExtendViewport viewport;

	private SpriteBatch batch;
	
	private Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));

	private Table table;

	private Label progressLabel;
	private Label healthLabel;
	private Label powerUpLabel;
	private Label bossLabel;

	public Hud(Zepr parent) {
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		stage = new Stage(viewport, batch);

		table = new Table();
		table.setFillParent(true);
		
		progressLabel = new Label("Wave 1", skin);
		healthLabel = new Label("Health: 100HP", skin);
		powerUpLabel = new Label("No powerup", skin);
		bossLabel = new Label("", skin);

		table.top().left();
		table.add(progressLabel).pad(10).left().row();
		table.add(healthLabel).pad(10).left().row();
		table.add(powerUpLabel).pad(10).left().row();
		table.add(bossLabel).pad(10).left();

		stage.addActor(table);
	}

	public void setProgressLabel(int waveNumber, int zombiesRemaining) {
		progressLabel.setText("Wave " + Integer.toString(waveNumber) + ", " + Integer.toString(zombiesRemaining)
				+ " zombies remaining");
	}

	public void setHealthLabel(int health) {
		healthLabel.setText("Health: " + Integer.toString(health) + "HP");
	}
	
	public void setPowerUpLabel(PowerUp.Type type) {
		if (type != null) {
			powerUpLabel.setText("Power up available: " + type);
		} else {
			powerUpLabel.setText("No powerup");
		}
	}
	
	public void setBossLabel(boolean activeBoss) {
		if (activeBoss) {
			bossLabel.setText("Active boss");
		} else {
			bossLabel.setText("");
		}
	}

	@Override
	public void dispose() {
		batch.dispose();
		stage.dispose();
	}

}
