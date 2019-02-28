package com.geeselightning.zepr.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.geeselightning.zepr.game.Zepr;
import com.geeselightning.zepr.minigame.MiniGame;
/**
<<<<<<< HEAD
 * The screen used to render the minigame. Implemented in assessment 3.
 * @author ljd546
 * @author Xzytl
 *
 */
public class MiniGameScreen extends DefaultScreen {
		
		private OrthographicCamera camera;
		private ExtendViewport gamePort;
		private SpriteBatch batch;
		private MiniGame miniGame;
		private Sprite backgroundSprite;
		private Label scoreLabel;
		private Label ammoLabel;
		private Label waveLabel;

//		Uncomment below for debug
//		public ShapeRenderer shapeRenderer;
		
		
		public MiniGameScreen(Zepr parent){
			super(parent);			
			this.miniGame = new MiniGame();
			float width = Gdx.graphics.getWidth();
			float height = Gdx.graphics.getHeight();

			this.camera = new OrthographicCamera(width, height);
			this.gamePort = new ExtendViewport(width, height);
			this.camera.position.set(gamePort.getWorldWidth() / 2, gamePort.getWorldHeight() / 2, 0);
			
			this.backgroundSprite = new Sprite(new Texture("miniGameBG.png"));
			this.backgroundSprite.setCenter(0, 0);
			this.batch = new SpriteBatch();	
			
//			shapeRenderer = new ShapeRenderer();
		}
		
		@Override
		public void show() {
			Gdx.input.setInputProcessor(this.miniGame.miniGameController);
			//Positions UI
			Skin skin = new Skin(Gdx.files.internal("skin/pixthulhu-ui.json"));
			scoreLabel = new Label("Score : ", skin);
			scoreLabel.setPosition(-600, 300);
			scoreLabel.toFront();
			ammoLabel = new Label("Ammo : ", skin);
			ammoLabel.setPosition(-600, 250);
			ammoLabel.toFront();
			waveLabel = new Label("Round : ",skin);
			waveLabel.setPosition(-600, 200);
			waveLabel.toFront();
		}

		@Override
		public void render(float delta) {
			super.render(delta);
			if (this.miniGame.active == false) {
				quit();
			}
			// Player can quit by pressing escape.
			if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
				quit();
			}
			this.miniGame.update(delta);
			//Rendering the background
			batch.setProjectionMatrix(camera.combined);
			batch.begin();
			backgroundSprite.draw(batch);

			// Renders goose
			if (!miniGame.goose.isDead) {
				miniGame.goose.draw(this.batch);
			}

			//Renders labels for score, ammo, wave number
			this.scoreLabel.setText("Score:   " + String.valueOf(miniGame.score));
			this.ammoLabel.setText("Ammo: " + String.valueOf(miniGame.ammo));
			this.waveLabel.setText("Round: " + String.valueOf(miniGame.wave));
			this.scoreLabel.draw(batch, 1f);
			this.ammoLabel.draw(batch, 1f);
			this.waveLabel.draw(batch, 1f);
			batch.end();
			
//			shapeRenderer.setProjectionMatrix(camera.combined);
//			shapeRenderer.begin(ShapeType.Line);
//			shapeRenderer.setColor(Color.RED);
//			//BOX AROUND GOOSE 
//		    shapeRenderer.rect(Gdx.input.getX() - Gdx.graphics.getWidth()/2,-1 * Gdx.input.getY() + Gdx.graphics.getHeight()/2, 20, 20);
//		    shapeRenderer.end();
		    
		    //DEBUG ENDS
		    
		}
		
		@Override
		public void resize(int width, int height) {
			gamePort.update(width, height);
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
			batch.dispose();
		}
		
		public void quit() {
			parent.changeScreen(Zepr.MENU);
		}
}

